package ru.surfstudio.android.core.mvi.sample.ui.screen.list.controller

import android.view.ViewGroup
import android.widget.TextView
import ru.surfstudio.android.core.mvi.sample.R
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

/**
 * Контроллер экрана [ComplexListActivityView]
 */
class ComplexListController : BindableItemController<String, ComplexListController.Holder>() {
    override fun getItemId(data: String): String = data

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    inner class Holder(parent: ViewGroup) : BindableViewHolder<String>(parent, R.layout.element_reactive_item) {

        override fun bind(data: String) {
            (itemView as TextView).text = data
        }
    }
}