package ru.surfstudio.android.network.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.model.LdsSwrPgnScreenModel
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.network.sample.domain.product.Product

class MainScreenModel(val productList: DataList<Product> = DataList.empty()
) : LdsSwrPgnScreenModel() {
    fun hasContent(): Boolean = productList.size > 0
}