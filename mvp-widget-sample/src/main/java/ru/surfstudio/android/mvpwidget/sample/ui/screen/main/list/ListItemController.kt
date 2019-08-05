package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.list

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_list_list.view.*
import ru.surfstudio.android.mvpwidget.sample.R
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

class ListItemController(
        val onItemClick: (String) -> Unit
) : BindableItemController<ListItem, ListItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(item: ListItem): String = item.id.toString()

    inner class Holder(parent: ViewGroup) : BindableViewHolder<ListItem>(parent, R.layout.item_list_list) {

        val constraintWidgetView = itemView.constraint_w

        override fun bind(item: ListItem) {
            constraintWidgetView.widgetDataId = "id:${item.id}_"
            constraintWidgetView.render(item.id.toString())
        }
    }
}
