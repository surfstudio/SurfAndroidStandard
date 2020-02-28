package ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers

import android.view.ViewGroup
import ru.surfstudio.android.easyadapter.controller.NoDataItemController
import ru.surfstudio.android.easyadapter.holder.BaseViewHolder
import ru.surfstudio.android.easyadapter.sample.R

class EmptyItemController : NoDataItemController<EmptyItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup?): Holder = Holder(parent)

    inner class Holder(parent: ViewGroup?) : BaseViewHolder(parent, R.layout.empty_item_controller)
}