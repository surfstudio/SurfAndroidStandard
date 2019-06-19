package ru.surfstudio.android.mvp.binding.rx.sample.react.controller

import android.view.ViewGroup
import android.widget.TextView
import ru.surfstudio.android.core.mvp.binding.sample.R
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

class ReactiveListController : BindableItemController<String, ReactiveListController.Holder>() {
    override fun getItemId(data: String): String = data

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    inner class Holder(parent: ViewGroup) : BindableViewHolder<String>(parent, R.layout.element_reactive_item) {

        override fun bind(data: String) {
            (itemView as TextView).text = data
        }
    }
}