package ru.surfstudio.android.recycler.extension.sample.controller

import android.view.ViewGroup
import android.widget.TextView
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.recycler.extension.sample.R

class SampleSnapHelperItemController()
    : BindableItemController<String, SampleSnapHelperItemController.Holder>() {

    override fun getItemId(data: String): String = data.hashCode().toString()

    override fun createViewHolder(parent: ViewGroup?): Holder = Holder(parent)

    inner class Holder(
            parent: ViewGroup?
    ) : BindableViewHolder<String>(parent, R.layout.item_list_snap_helper) {

        private var nameTv: TextView = itemView.findViewById(R.id.list_item_snap_name)

        override fun bind(data: String) {
            nameTv.text = data
        }
    }
}