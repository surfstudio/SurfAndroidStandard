package ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.TextView
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.easyadapter.sample.R
import ru.surfstudio.android.easyadapter.sample.domain.SecondData

class SecondDataItemController(
        private val onClickListener: (SecondData) -> Unit
) : BindableItemController<SecondData, SecondDataItemController.Holder>() {

    override fun getItemId(data: SecondData): String = data.hashCode().toString()

    override fun createViewHolder(parent: ViewGroup?): Holder = Holder(parent)

    inner class Holder(
            parent: ViewGroup?
    ) : BindableViewHolder<SecondData>(parent, R.layout.second_data_item_controller) {

        private lateinit var data: SecondData
        private val secondTv: TextView = itemView.findViewById(R.id.second_tv)

        init {
            itemView.setOnClickListener { onClickListener(data) }
        }

        @SuppressLint("SetTextI18n")
        override fun bind(data: SecondData) {
            this.data = data
            secondTv.text = data.stringValue
        }
    }
}