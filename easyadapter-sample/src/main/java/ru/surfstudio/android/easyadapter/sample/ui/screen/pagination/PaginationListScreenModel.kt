package ru.surfstudio.android.easyadapter.sample.ui.screen.pagination

import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.easyadapter.sample.domain.FirstData

class PaginationListScreenModel : ScreenModel() {

    var list: List<FirstData> = ArrayList()
    var page = 0
}