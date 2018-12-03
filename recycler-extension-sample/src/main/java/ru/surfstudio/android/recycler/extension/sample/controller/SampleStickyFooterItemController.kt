package ru.surfstudio.android.recycler.extension.sample.controller

import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_list_footer_sample.*
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.recycler.extension.sample.R
import ru.surfstudio.android.recycler.extension.sticky.controller.StickyFooterBindableItemController
import ru.surfstudio.android.recycler.extension.sticky.item.StickyFooterBindableItem

class SampleStickyFooterItemController
    : StickyFooterBindableItemController<String, SampleStickyFooterItemController.Holder>() {

    override fun getItemId(item: StickyFooterBindableItem<String, Holder>) =
            item.data.hashCode().toString()

    override fun createViewHolder(parent: ViewGroup?): Holder =
            Holder(parent)

    inner class Holder(parent: ViewGroup?) : BindableViewHolder<String>(parent, R.layout.item_list_footer_sample) {

        override fun bind(data: String?) {
            item_list_sample_footer_tv.text = data
        }
    }
}