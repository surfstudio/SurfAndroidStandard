package ru.surfstudio.android.recycler.extension.sample.controller

import android.view.ViewGroup
import android.widget.TextView
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.recycler.extension.sample.R
import ru.surfstudio.android.recycler.extension.sticky.controller.StickyHeaderBindableItemController
import ru.surfstudio.android.recycler.extension.sticky.item.StickyHeaderBindableItem

class SampleStickyHeaderItemController
    : StickyHeaderBindableItemController<String, SampleStickyHeaderItemController.Holder>() {

    override fun getItemId(item: StickyHeaderBindableItem<String, Holder>) = item.data.hashCode().toString()

    override fun createViewHolder(parent: ViewGroup?): Holder = Holder(parent)

    inner class Holder(parent: ViewGroup?) : BindableViewHolder<String>(parent, R.layout.item_list_header_sample) {
        private val textView = itemView.findViewById<TextView>(R.id.item_list_sample_header_tv)
        override fun bind(data: String?) {
            textView.text = data
        }
    }
}