package ru.surfstudio.standard.f_debug.controller

import android.view.ViewGroup
import kotlinx.android.synthetic.main.debug_layout.view.*
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.template.f_debug.R

/**
 * Базовый класс для контроллеров каталога debug-экрана
 */
abstract class BaseDebugItemController(
        private val onClickListener: () -> Unit
) : BindableItemController<String, BaseDebugItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(name: String): String = name.hashCode().toString()

    inner class Holder(parent: ViewGroup
    ) : BindableViewHolder<String>(parent, R.layout.debug_layout) {

        init {
            itemView.setOnClickListener { onClickListener.invoke() }
        }

        override fun bind(name: String) {
            itemView.debug_item_tv.text = name
        }
    }
}