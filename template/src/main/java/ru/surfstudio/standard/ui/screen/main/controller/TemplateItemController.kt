package ru.surfstudio.standard.ui.screen.main.controller;

import android.view.ViewGroup
import ru.surfstudio.android.easyadapter.impl.controller.BindableItemController
import ru.surfstudio.android.easyadapter.impl.holder.BindableViewHolder
import ru.surfstudio.standard.R

class TemplateItemController() : BindableItemController<Int, TemplateItemController.Holder>() {
    override fun getItemId(data: Int?): Long = data?.toLong() ?: 0L

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    inner class Holder(parent: ViewGroup) : BindableViewHolder<Int>(parent, R.layout.template_layout) {
        override fun bind(data: Int?) {
            // stub!
        }
    }
}
