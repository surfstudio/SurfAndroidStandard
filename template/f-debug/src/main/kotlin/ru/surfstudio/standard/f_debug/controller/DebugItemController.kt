package ru.surfstudio.standard.f_debug.controller

import android.view.ViewGroup
import kotlinx.android.synthetic.main.debug_layout.view.*
import ru.surfstudio.android.easyadapter.controller.NoDataItemController
import ru.surfstudio.android.easyadapter.holder.BaseViewHolder
import ru.surfstudio.android.easyadapter.item.NoDataItem
import ru.surfstudio.android.template.f_debug.R

class DebugItemController(
        private val name: String,
        private val onClickListener: () -> Unit
) : NoDataItemController<DebugItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(item: NoDataItem<Holder>?): String = name.hashCode().toString()

    override fun getItemHash(item: NoDataItem<Holder>?): String = getItemId(item)

    inner class Holder(parent: ViewGroup) : BaseViewHolder(parent, R.layout.debug_layout) {
        init {
            itemView.debug_item_tv.text = name
            itemView.setOnClickListener { onClickListener.invoke() }
        }
    }
}