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
package ru.surfstudio.android.datalistpagecount.util;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList;
import ru.surfstudio.android.rx.extension.FunctionSafe;
import ru.surfstudio.android.rx.extension.ObservableUtil;

public class PaginationableUtil {

    /**
     * Создает запрос составленный из нескольких запросов, каждый из которых загружает блок данных
     * размером blockSize.
     * Такое разбиение необходимо чтобы при обновлении данных списка они кешировались блоками с размером,
     * который используется при подгрузке новых данных.
     *
     * @param paginationRequestCreator функция, создающая один из подзапросов, имеет 1 параметр page
     * @param numPages                 Клоличество страниц которые необходимо загрузить
     * @return Observable, который эмитит необходимый блок данных, может эмитить несколько раз из-за
     * combineLatestDelayError
     */
    private static <T, L extends DataList<T>> Observable<L> getPaginationRequestPortions(
            FunctionSafe<Integer, Observable<L>> paginationRequestCreator,
            L emptyValue,
            int numPages) {
        List<Observable<? extends L>> portionRequests = new ArrayList<>();
        for (int i = 0; i < numPages; i++) {
            portionRequests.add(paginationRequestCreator.apply(i));
        }
        if (portionRequests.isEmpty()) {
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

    /**
     * Создает запрос составленный из нескольких запросов, каждый из которых загружает блок данных
     * размером blockSize.
     * Такое разбиение необходимо чтобы при обновлении данных списка они кешировались блоками с размером,
     * который используется при подгрузке новых данных.
     *
     * @param paginationRequestCreator функция, создающая один из подзапросов, имеет 1 параметр page
     * @param numPages                 Клоличество страниц которые необходимо загрузить
     * @return Observable, который эмитит необходимый блок данных, может эмитить несколько раз из-за
     * combineLatestDelayError
     */
    public static <T> Observable<DataList<T>> getPaginationRequestPortions(
            FunctionSafe<Integer, Observable<DataList<T>>> paginationRequestCreator,
            int numPages) {
        return getPaginationRequestPortions(
                paginationRequestCreator,
                DataList.empty(),
                numPages);
    }

    public static <T> Single<DataList<T>> getPaginationSingleRequestPortion(
            FunctionSafe<Integer, Single<DataList<T>>> paginationRequestCreator,
            int numPages) {
        return getPaginationRequestPortions(
                convertSingleBiFunctionToObservable(paginationRequestCreator),
                DataList.empty(),
                numPages).singleOrError();
    }

    private static <T> FunctionSafe<Integer, Observable<T>> convertSingleBiFunctionToObservable(FunctionSafe<Integer, Single<T>> paginationRequestCreator) {
        return (integer) -> paginationRequestCreator.apply(integer).toObservable();
    }

}