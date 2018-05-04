/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.datalistlimitoffset.util;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList;
import ru.surfstudio.android.rx.extension.BiFunctionSafe;
import ru.surfstudio.android.rx.extension.ObservableUtil;

public class PaginationableUtil {

    /**
     * Создает запрос составленный из нескольких запросов, каждый из которых загружает блок данных
     * размером blockSize.
     * Такое разбиение необходимо чтобы при обновлении данных списка они кешировались блоками с размером,
     * который используется при подгрузке новых данных.
     *
     * @param paginationRequestCreator функции создающая один из подзапросов, имеет 2 параметра limit и offset
     * @param offset                   смещение с которого следует подгрузить данные
     * @param limit                    размер загружаемых данных
     * @param blockSize                размер подгружаемого блока
     * @return Observable, который эмитит необходимый блок данных, может эмитить несколько раз из-за
     * combineLatestDelayError
     */
    public static <T> Observable<DataList<T>> getPaginationRequestPortions(
            BiFunctionSafe<Integer, Integer, Observable<DataList<T>>> paginationRequestCreator,
            int offset, int limit, int blockSize) {
        return getPaginationRequestPortions(paginationRequestCreator,
                new DataList<>(new ArrayList<>(), limit, offset, 0),
                offset, limit, blockSize);
    }

    public static <T> Single<DataList<T>> getPaginationSingleRequestPortion(
            BiFunctionSafe<Integer, Integer, Single<DataList<T>>> paginationRequestCreator,
            int offset, int limit, int blockSize) {
        return getPaginationRequestPortions(convertSingleBiFunctionToObservable(paginationRequestCreator),
                offset, limit, blockSize).singleOrError();
    }

    /**
     * Создает запрос составленный из нескольких запросов, каждый из которых загружает блок данных
     * размером blockSize.
     * Такое разбиение необходимо чтобы при обновлении данных списка они кешировались блоками с размером,
     * который используется при подгрузке новых данных.
     *
     * @param paginationRequestCreator функции создающая один из подзапросов, имеет 2 параметра limit и offset
     * @param offset                   смещение с которого следует подгрузить данные
     * @param limit                    размер загружаемых данных
     * @param blockSize                размер подгружаемого блока
     * @param totalCount               максимальное число элементов
     * @return Observable, который эмитит необходимый блок данных, может эмитить несколько раз из-за
     * combineLatestDelayError
     */
    public static <T> Observable<DataList<T>> getPaginationRequestPortionsWithTotal(
            BiFunctionSafe<Integer, Integer, Observable<DataList<T>>> paginationRequestCreator,
            int offset, int limit, int blockSize, int totalCount) {
        return getPaginationRequestPortions(paginationRequestCreator,
                new DataList<>(new ArrayList<>(), limit, offset, totalCount),
                offset, limit, blockSize);
    }

    public static <T> Single<DataList<T>> getPaginationRequestSinglePortionWithTotal(
            BiFunctionSafe<Integer, Integer, Single<DataList<T>>> paginationRequestCreator,
            int offset, int limit, int blockSize, int totalCount) {
        return getPaginationRequestPortionsWithTotal(convertSingleBiFunctionToObservable(paginationRequestCreator),
                offset, limit, blockSize, totalCount).singleOrError();
    }

    /**
     * Создает запрос составленный из нескольких запросов, каждый из которых загружает блок данных
     * размером blockSize.
     * Такое разбиение необходимо чтобы при обновлении данных списка они кешировались блоками с размером,
     * который используется при подгрузке новых данных.
     *
     * @param paginationRequestCreator функции создающая один из подзапросов, имеет 2 параметра limit и offset
     * @param offset                   смещение с которого следует подгрузить данные
     * @param limit                    размер загружаемых данных
     * @param blockSize                размер подгружаемого блока
     * @return Observable, который эмитит необходимый блок данных, может эмитить несколько раз из-за
     * combineLatestDelayError
     */
    private static <T, L extends DataList<T>> Observable<L> getPaginationRequestPortions(
            BiFunctionSafe<Integer, Integer, Observable<L>> paginationRequestCreator,
            L emptyValue,
            int offset, int limit, int blockSize) {
        List<Observable<? extends L>> portionRequests = new ArrayList<>();
        for (; offset < limit; offset += blockSize) {
            portionRequests.add(paginationRequestCreator.apply(blockSize, offset));
        }
        if (portionRequests.size() == 0) {
            portionRequests.add(Observable.just(emptyValue));
        }
        return ObservableUtil.combineLatestDelayError(Schedulers.trampoline(), portionRequests,
                portions -> {
                    L result = null;
                    for (Object rawPortion : portions) {
                        L portion = (L) rawPortion;
                        if (result == null) {
                            result = portion;
                        } else {
                            result.merge(portion);
                        }
                    }
                    return result;
                });
    }

    private static <T> BiFunctionSafe<Integer, Integer, Observable<T>> convertSingleBiFunctionToObservable(BiFunctionSafe<Integer, Integer, Single<T>> paginationRequestCreator) {
        return (integer, integer2) -> paginationRequestCreator.apply(integer, integer2).toObservable();
    }

}