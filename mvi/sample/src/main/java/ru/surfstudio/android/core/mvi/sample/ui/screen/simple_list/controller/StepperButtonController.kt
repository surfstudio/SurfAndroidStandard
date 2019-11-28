package ru.surfstudio.android.core.mvi.sample.ui.screen.simple_list.controller

import android.view.ViewGroup
import android.widget.Button
import ru.surfstudio.android.core.mvi.sample.R
import ru.surfstudio.android.core.mvp.binding.BindableView
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

/**
 * Контроллер с кнопками, увеличивающими свое значение при каждом нажатии
 */
class StepperButtonController(
        private val onBtnClick: (position: Int) -> Unit
) : BindableItemController<Int, StepperButtonController.Holder>() {

    override fun getItemId(data: Int): String = data.toString()

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    inner class Holder(parent: ViewGroup) : BindableViewHolder<Int>(parent, R.layout.element_stepper_btn) {

        val button = itemView as Button

        override fun bind(data: Int) {
            button.setOnClickListener { onBtnClick(adapterPosition) }
            button.text = "Taps: $data"

        }
    }
}