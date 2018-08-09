package ru.surfstudio.android.easyadapter.sample.ui.screen.pagination

import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.easyadapter.sample.domain.FirstData

class PaginationListScreenModel : ScreenModel() {

    private val list = ArrayList<FirstData>()
    private var page = 0

    companion object {
        private const val DATA_SIZE = 150
        private const val PAGE_SIZE = 15
    }

    init {
        (0..DATA_SIZE).forEach { list.add(FirstData(it)) }
    }

    fun getData(): List<FirstData> {
        val result = list.subList(page, page + PAGE_SIZE)
        page++
        return result
    }
}