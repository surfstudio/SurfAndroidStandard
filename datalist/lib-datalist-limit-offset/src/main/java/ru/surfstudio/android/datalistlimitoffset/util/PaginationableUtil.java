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
     * Creates Observable request with multiple subrequests for blocks of data with size of blockSize.
     * Such block division is used for caching blocks with size of ordinary pagination data loading.
     * Could emit it several times due to combineLatestDelayError
     *
     * @param paginationRequestCreator subrequests creator function. Has two params: limit and offset.
     * @param offset                   start offset
     * @param limit                    size of all data blocks
     * @param blockSize                size of one data block
     * @return Observable, which emits blocks of data.
     */
    public static <T> Observable<DataList<T>> getPaginationRequestPortions(
            BiFunctionSafe<Integer, Integer, Observable<DataList<T>>> paginationRequestCreator,
            int offset, int limit, int blockSize) {
        return getPaginationRequestPortions(paginationRequestCreator,
                new DataList<>(new ArrayList<>(), limit, offset, 0),
                offset, limit, blockSize);
    }

    /**
     * Creates Single request with multiple subrequests for blocks of data with size of blockSize.
     * Such block division is used for caching blocks with size of ordinary pagination data loading.
     *
     * @param paginationRequestCreator subrequests creator function. Has two params: limit and offset.
     * @param offset                   start offset
     * @param limit                    size of all data blocks
     * @param blockSize                size of one data block
     * @return Observable, which emits blocks of data.
     */
    public static <T> Single<DataList<T>> getPaginationSingleRequestPortion(
            BiFunctionSafe<Integer, Integer, Single<DataList<T>>> paginationRequestCreator,
            int offset, int limit, int blockSize) {
        return getPaginationRequestPortions(convertSingleBiFunctionToObservable(paginationRequestCreator),
                offset, limit, blockSize).singleOrError();
    }

    /**
     * Creates Observable request with multiple subrequests for blocks of data with size of blockSize.
     * Such block division is used for caching blocks with size of ordinary pagination data loading.
     * Could emit it several times due to combineLatestDelayError
     *
     * @param paginationRequestCreator subrequests creator function. Has two params: limit and offset.
     * @param offset                   start offset
     * @param limit                    size of all data blocks
     * @param blockSize                size of one data block
     * @param totalCount               maximum number of elements in list
     * @return Observable, which emits blocks of data.
     */
    public static <T> Observable<DataList<T>> getPaginationRequestPortionsWithTotal(
            BiFunctionSafe<Integer, Integer, Observable<DataList<T>>> paginationRequestCreator,
            int offset, int limit, int blockSize, int totalCount) {
        return getPaginationRequestPortions(paginationRequestCreator,
                new DataList<>(new ArrayList<>(), limit, offset, totalCount),
                offset, limit, blockSize);
    }

    /**
     * Creates Single request with multiple subrequests for blocks of data with size of blockSize.
     * Such block division is used for caching blocks with size of ordinary pagination data loading.
     *
     * @param paginationRequestCreator subrequests creator function. Has two params: limit and offset.
     * @param offset                   start offset
     * @param limit                    size of all data blocks
     * @param blockSize                size of one data block
     * @param totalCount               maximum number of elements in list
     * @return Single, which emits blocks of data.
     */
    public static <T> Single<DataList<T>> getPaginationRequestSinglePortionWithTotal(
            BiFunctionSafe<Integer, Integer, Single<DataList<T>>> paginationRequestCreator,
            int offset, int limit, int blockSize, int totalCount) {
        return getPaginationRequestPortionsWithTotal(convertSingleBiFunctionToObservable(paginationRequestCreator),
                offset, limit, blockSize, totalCount).singleOrError();
    }

    /**
     * Creates Observable request with multiple subrequests for blocks of data with size of blockSize.
     * Such block division is used for caching blocks with size of ordinary pagination data loading.
     * Could emit it several times due to combineLatestDelayError
     *
     * @param paginationRequestCreator subrequests creator function. Has two params: limit and offset.
     * @param emptyValue               DataList used to hold elements
     * @param offset                   start offset
     * @param limit                    size of all data blocks
     * @param blockSize                size of one data block
     * @return Observable, which emits blocks of data.
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