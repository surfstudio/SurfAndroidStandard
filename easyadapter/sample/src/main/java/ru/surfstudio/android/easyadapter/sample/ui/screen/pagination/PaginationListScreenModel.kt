package ru.surfstudio.android.easyadapter.sample.ui.screen.pagination

import ru.surfstudio.android.core.mvp.model.LdsPgnScreenModel
import ru.surfstudio.android.easyadapter.sample.domain.FirstData
import ru.surfstudio.android.easyadapter.sample.interactor.DataListLimitOffset
import ru.surfstudio.android.easyadapter.sample.interactor.DataListPageCount

class PaginationListScreenModel : LdsPgnScreenModel() {

    val pageList: DataListPageCount<FirstData> = DataListPageCount.empty()
    val limitOffsetList: DataListLimitOffset<FirstData> = DataListLimitOffset.empty()
}