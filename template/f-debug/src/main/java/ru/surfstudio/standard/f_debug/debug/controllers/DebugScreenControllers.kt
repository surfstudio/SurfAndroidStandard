package ru.surfstudio.standard.f_debug.debug.controllers

import android.view.ViewGroup
import kotlinx.android.synthetic.main.base_debug_controller_layout.view.*
import kotlinx.android.synthetic.main.custom_controller_description_layout.view.*
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
    ) : BindableViewHolder<String>(parent, R.layout.base_debug_controller_layout) {

        init {
            itemView.setOnClickListener { onClickListener.invoke() }
        }

        override fun bind(name: String) {
            itemView.debug_item_tv.text = name
        }
    }
}

/**
 * Контроллер для описания кастомного контроллера
 */
class CustomControllerDescriptionItemController
    : BindableItemController<String, CustomControllerDescriptionItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(string: String): String = string.hashCode().toString()

    inner class Holder(parent: ViewGroup
    ) : BindableViewHolder<String>(parent, R.layout.custom_controller_description_layout) {

        override fun bind(string: String) {
            itemView.description_tv.text = string
        }
    }
}