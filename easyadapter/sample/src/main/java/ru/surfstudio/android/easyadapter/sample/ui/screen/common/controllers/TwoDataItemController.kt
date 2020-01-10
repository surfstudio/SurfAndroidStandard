package ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers

import android.view.ViewGroup
import android.widget.TextView
import ru.surfstudio.android.easyadapter.controller.DoubleBindableItemController
import ru.surfstudio.android.easyadapter.holder.DoubleBindableViewHolder
import ru.surfstudio.android.easyadapter.sample.R
import ru.surfstudio.android.easyadapter.sample.domain.FirstData
import ru.surfstudio.android.easyadapter.sample.domain.SecondData

class TwoDataItemController
    : DoubleBindableItemController<FirstData, SecondData, TwoDataItemController.Holder>() {

    override fun getItemId(firstData: FirstData, secondData: SecondData): String {
        return (firstData.toString() + secondData.stringValue).hashCode().toString()
    }

    override fun createViewHolder(parent: ViewGroup?): Holder = Holder(parent)

    inner class Holder(
            parent: ViewGroup?
    ) : DoubleBindableViewHolder<FirstData, SecondData>(parent, R.layout.two_data_item_controller) {

        private val firstTv: TextView = itemView.findViewById(R.id.first_tv)
        private val secondTv: TextView = itemView.findViewById(R.id.second_tv)

        override fun bind(firstData: FirstData, secondData: SecondData) {
            firstTv.text = firstData.toString()
            secondTv.text = secondData.stringValue
        }
    }
}