package ru.surfstudio.android.sample.common.ui.base.loadstate.presentations.controllers

import android.view.ViewGroup
import ru.surfstudio.android.easyadapter.controller.NoDataItemController
import ru.surfstudio.android.easyadapter.holder.BaseViewHolder
import ru.surfstudio.android.sample.common.R


/**
 * Контроллер для пустых состний в списках
 * */

class ErrorLoadStateController : NoDataItemController<ErrorLoadStateController.Holder>() {

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) : BaseViewHolder(parent, R.layout.load_state_error_controller)
}