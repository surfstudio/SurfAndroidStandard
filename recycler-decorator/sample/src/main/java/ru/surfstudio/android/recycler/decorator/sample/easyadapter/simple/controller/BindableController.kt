package ru.surfstudio.android.recycler.decorator.sample.easyadapter.simple.controller

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.item_controller_short_card.view.*
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

class BindableController(@LayoutRes val layoutRes: Int) : BindableItemController<Int, BindableController.Holder>() {

    override fun viewType(): Int {
        return layoutRes
    }

    override fun createViewHolder(parent: ViewGroup): Holder =
            Holder(parent, layoutRes)

    override fun getItemId(data: Int): String {
        return "$data"
    }

    class Holder(parent: ViewGroup, layoutRes: Int) : BindableViewHolder<Int>(parent, layoutRes) {

        override fun bind(number: Int) {
            itemView.number_tv.text = "$number"
        }
    }

}