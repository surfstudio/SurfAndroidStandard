package ru.surfstudio.android.easyadapter.sample.interactor

import io.reactivex.Observable
import ru.surfstudio.android.easyadapter.sample.domain.FirstData
import javax.inject.Inject

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

    fun getData(page: Int): Observable<List<FirstData>> {
        val startIndex = PAGE_SIZE * page
        return Observable.just(list.subList(startIndex, startIndex + PAGE_SIZE))
    }
}