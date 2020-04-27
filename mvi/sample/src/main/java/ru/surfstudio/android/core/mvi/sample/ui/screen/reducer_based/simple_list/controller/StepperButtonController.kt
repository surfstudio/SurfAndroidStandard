package ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.simple_list.controller

import android.view.ViewGroup
import android.widget.Button
import ru.surfstudio.android.core.mvi.sample.R
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

/**
 * Контроллер с кнопками, увеличивающими свое значение при каждом нажатии
 */
class StepperButtonController(
        private val onBtnClick: (position: Int) -> Unit
) : BindableItemController<StepperData, StepperButtonController.Holder>() {

    override fun getItemId(data: StepperData): String = data.id.toString()

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    inner class Holder(parent: ViewGroup) : BindableViewHolder<StepperData>(parent, R.layout.element_stepper_btn) {

        val button = itemView as Button

        override fun bind(data: StepperData) {
            button.setOnClickListener { onBtnClick(data.id) }
            button.text = "Clicks: ${data.clicks}"

        }
    }
}