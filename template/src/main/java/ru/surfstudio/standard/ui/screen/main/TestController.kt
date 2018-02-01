package ru.surfstudio.standard.ui.screen.main

import android.view.ViewGroup
import android.widget.TextView
import ru.surfstudio.android.core.ui.base.recycler.controller.BindableItemController
import ru.surfstudio.android.core.ui.base.recycler.holder.BindableViewHolder
import ru.surfstudio.standard.R

/**
 * Created by vsokolova on 2/1/18.
 */
class TestController : BindableItemController<TestItem, TestController.Holder>() {

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    override fun getItemId(data: TestItem): Long {
        return data.title.hashCode().toLong()
    }

    inner class Holder(parent: ViewGroup) : BindableViewHolder<TestItem>(parent, R.layout.item_test_controller) {
        val titleTv: TextView = itemView.findViewById(R.id.item_test_controller_tv)

        override fun bind(data: TestItem) {
            titleTv.text = data.title
        }

    }
}