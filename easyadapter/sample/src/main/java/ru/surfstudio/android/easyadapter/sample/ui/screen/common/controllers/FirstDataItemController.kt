package ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.TextView
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.easyadapter.sample.R
import ru.surfstudio.android.easyadapter.sample.domain.FirstData

class FirstDataItemController(
        private val onClickListener: (FirstData) -> Unit
) : BindableItemController<FirstData, FirstDataItemController.Holder>() {

    override fun getItemId(data: FirstData): String = data.hashCode().toString()

    override fun createViewHolder(parent: ViewGroup?): Holder = Holder(parent)

    inner class Holder(
            parent: ViewGroup?
    ) : BindableViewHolder<FirstData>(parent, R.layout.first_data_item_controller) {

        private lateinit var data: FirstData
        private val firstTv: TextView = itemView.findViewById(R.id.first_tv)

        init {
            itemView.setOnClickListener { onClickListener(data) }
        }

        @SuppressLint("SetTextI18n")
        override fun bind(data: FirstData) {
            this.data = data
            firstTv.text = "Value = $data"
        }
    }
}