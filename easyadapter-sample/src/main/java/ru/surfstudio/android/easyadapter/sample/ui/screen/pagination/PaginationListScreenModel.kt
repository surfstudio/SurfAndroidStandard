package ru.surfstudio.android.easyadapter.sample.ui.screen.pagination

import ru.surfstudio.android.core.mvp.model.LdsPgnScreenModel
import ru.surfstudio.android.easyadapter.sample.domain.FirstData

class PaginationListScreenModel : LdsPgnScreenModel() {

    var list: MutableList<FirstData> = ArrayList()
    var page = 0
}