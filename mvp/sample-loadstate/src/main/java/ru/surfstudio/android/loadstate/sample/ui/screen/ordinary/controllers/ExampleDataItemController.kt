package ru.surfstudio.android.loadstate.sample.ui.screen.ordinary.controllers

import android.view.ViewGroup
import android.widget.TextView
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.loadstate.sample.R

class ExampleDataItemController : BindableItemController<Int, ExampleDataItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(int: Int): String = int.toString()

    inner class Holder(parent: ViewGroup) : BindableViewHolder<Int>(parent, R.layout.item_list_example_data) {

        private val tv: TextView = itemView.findViewById(R.id.item_list_example_data_tv)

        override fun bind(value: Int) {
            tv.text = itemView.resources.getString(R.string.item_list_example_data_text, value)
        }
    }
}
