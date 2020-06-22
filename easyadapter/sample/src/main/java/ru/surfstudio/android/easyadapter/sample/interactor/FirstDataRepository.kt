package ru.surfstudio.android.easyadapter.sample.interactor

import io.reactivex.Observable
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.easyadapter.sample.domain.FirstData
import javax.inject.Inject

// aliases for different DataList distinguishing. In real project only one will be used
typealias DataListPageCount<T> = DataList<T>
typealias DataListLimitOffset<T> = ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList<T>

class FirstDataRepository @Inject constructor() {

    private val list = ArrayList<FirstData>()

    companion object {
        private const val DATA_SIZE = 150
        private const val PAGE_SIZE = 15

        const val PAGES_COUNT = DATA_SIZE / PAGE_SIZE

        // page number which will be used for PaginationState.ERROR setting
        // in order to demonstrate adapter's footer
        const val ERROR_PAGE_NUMBER = PAGES_COUNT / 2
    }

    init {
        (1..DATA_SIZE + 1).forEach { list.add(FirstData(it)) }
    }

    /**
     * Load data for page number
     */
    fun getDataByPage(page: Int): Observable<DataListPageCount<FirstData>> {
        val startIndex = PAGE_SIZE * (page - 1)
        return Observable.just(
                DataList(
                        list.subList(startIndex, startIndex + PAGE_SIZE),
                        page,
                        PAGES_COUNT,
                        PAGE_SIZE
                )
        )
    }

    /**
     * Load data for offset
     */
    fun getDataByOffset(offset: Int, limit: Int = 15) =
            Observable.just(DataListLimitOffset(
                    list.subList(offset, offset + limit),
                    limit,
                    offset,
                    DATA_SIZE)
            )
}