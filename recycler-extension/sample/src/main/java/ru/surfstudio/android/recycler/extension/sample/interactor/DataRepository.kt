package ru.surfstudio.android.recycler.extension.sample.interactor

import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.recycler.extension.sample.domain.Data

class DataRepository {

    private val list = ArrayList<Data>()

    companion object {
        private const val DATA_SIZE = 150
        private const val PAGE_SIZE = 15

        const val PAGES_COUNT = DATA_SIZE / PAGE_SIZE
    }

    init {
        (1..DATA_SIZE + 1).forEach { list.add(Data(it.toString(), it.toString())) }
    }

    fun getDataByPage(page: Int): DataList<Data> {
        val startIndex = PAGE_SIZE * (page - 1)
        return DataList(
                list.subList(startIndex, startIndex + PAGE_SIZE),
                page,
                PAGES_COUNT,
                PAGE_SIZE
        )
    }
}