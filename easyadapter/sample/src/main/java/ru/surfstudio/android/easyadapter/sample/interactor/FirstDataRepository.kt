package ru.surfstudio.android.easyadapter.sample.interactor

import io.reactivex.Observable
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.easyadapter.sample.domain.FirstData
import javax.inject.Inject

//алиасы для различения двух дата-листов. В реальном проекте используется один из них.
typealias DataListPageCount<T> = ru.surfstudio.android.datalistpagecount.domain.datalist.DataList<T>
typealias DataListLimitOffset<T> = ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList<T>

class FirstDataRepository @Inject constructor() {

    private val list = ArrayList<FirstData>()

    companion object {
        private const val DATA_SIZE = 150
        private const val PAGE_SIZE = 15

        const val PAGES_COUNT = DATA_SIZE / PAGE_SIZE

        // Номер страницы, для которого будет установлен PaginationState.ERROR
        // для демонстрации футера адаптера
        const val ERROR_PAGE_NUMBER = PAGES_COUNT / 2
    }

    init {
        (1..DATA_SIZE + 1).forEach { list.add(FirstData(it)) }
    }

    /**
     * Загрузка данных по номеру страницы
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
     * Загрузка данных по смещению
     */
    fun getDataByOffset(offset: Int, limit: Int = 15) =
            Observable.just(DataListLimitOffset(
                    list.subList(offset, offset + limit),
                    limit,
                    offset,
                    DATA_SIZE)
            )
}