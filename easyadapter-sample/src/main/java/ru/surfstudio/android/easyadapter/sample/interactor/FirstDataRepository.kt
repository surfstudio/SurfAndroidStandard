package ru.surfstudio.android.easyadapter.sample.interactor

import io.reactivex.Observable
import ru.surfstudio.android.easyadapter.sample.domain.FirstData
import javax.inject.Inject

class FirstDataRepository @Inject constructor() {

    private val list = ArrayList<FirstData>()

    companion object {
        private const val DATA_SIZE = 150
        private const val PAGE_SIZE = 15
    }

    init {
        (0..DATA_SIZE).forEach { list.add(FirstData(it)) }
    }

    fun getData(page: Int): Observable<List<FirstData>> {
        return Observable.just(list.subList(page, page + PAGE_SIZE))
    }
}