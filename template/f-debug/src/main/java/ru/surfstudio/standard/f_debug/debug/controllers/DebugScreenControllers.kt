package ru.surfstudio.standard.f_debug.debug.controllers

import android.support.annotation.LayoutRes
import android.view.ViewGroup
import kotlinx.android.synthetic.main.base_debug_controller_layout.view.*
import kotlinx.android.synthetic.main.custom_controller_description_layout.view.*
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.controller.DoubleBindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.easyadapter.holder.DoubleBindableViewHolder
import ru.surfstudio.android.template.f_debug.R

typealias Listener = () -> Unit

/**
 * Базоый контроллер для элементов каталога debug-экрана
 */
abstract class BaseDebugItemController
    : DoubleBindableItemController<String, Listener, DebugViewHolder>() {

    override fun getItemId(name: String, listener: Listener): String = name.hashCode().toString()
}

/**
 * ViewHolder для debug-контроллеров
 */
class DebugViewHolder(
        parent: ViewGroup,
        @LayoutRes layoutId: Int = R.layout.base_debug_controller_layout
): DoubleBindableViewHolder<String, Listener>(parent, layoutId) {

    override fun bind(name: String, listener: Listener) {
        itemView.debug_item_tv.text = name
        itemView.setOnClickListener { listener.invoke() }
    }
}

/**
 * Контроллер для перехода на экран просмотра кастомных контроллеров
 */
class ShowDebugControllersDebugItemController : BaseDebugItemController() {

    override fun createViewHolder(parent: ViewGroup): DebugViewHolder = DebugViewHolder(parent)
}

/**
 * Контроллер для показа fcm-токена
 */
class ShowFcmTokenDebugItemController : BaseDebugItemController() {

    override fun createViewHolder(parent: ViewGroup): DebugViewHolder {
        return DebugViewHolder(parent, R.layout.fcm_debug_controller_layout)
    }
}

/**
 * Контроллер для описания кастомного контроллера
 */
class CustomControllerDescriptionItemController
    : BindableItemController<String, CustomControllerDescriptionItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(string: String): String = string.hashCode().toString()

    inner class Holder(
            parent: ViewGroup
    ): BindableViewHolder<String>(parent, R.layout.custom_controller_description_layout) {

        override fun bind(string: String) {
            itemView.description_tv.text = string
        }
    }
}