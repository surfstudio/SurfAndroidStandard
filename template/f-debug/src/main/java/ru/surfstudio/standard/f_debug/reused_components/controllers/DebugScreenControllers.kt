package ru.surfstudio.standard.f_debug.reused_components.controllers

import android.view.ViewGroup
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.android.template.f_debug.databinding.CustomControllerDescriptionLayoutBinding

/**
 * Контроллер для описания кастомного переиспользуемого в приложении контроллера
 */
class CustomControllerDescriptionItemController
    : BindableItemController<String, CustomControllerDescriptionItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(string: String): String = string.hashCode().toString()

    inner class Holder(
            parent: ViewGroup
    ): BindableViewHolder<String>(parent, R.layout.custom_controller_description_layout) {

        private val binding = CustomControllerDescriptionLayoutBinding.bind(itemView)

        override fun bind(string: String) {
            binding.debugDescriptionTv.text = string
        }
    }
}