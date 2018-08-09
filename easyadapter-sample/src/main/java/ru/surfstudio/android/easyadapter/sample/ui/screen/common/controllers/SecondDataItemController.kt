package ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.easyadapter.sample.R
import ru.surfstudio.android.easyadapter.sample.domain.SecondData

class SecondDataItemController(private val listener: SecondDataClickListener
) : BindableItemController<SecondData, SecondDataItemController.Holder>() {

    override fun getItemId(data: SecondData): Long = data.hashCode().toLong()

    override fun createViewHolder(parent: ViewGroup?): Holder = Holder(listener, parent)

    inner class Holder(listener: SecondDataClickListener,
                       parent: ViewGroup?
    ) : BindableViewHolder<SecondData>(parent, R.layout.second_data_item_controller) {

        private lateinit var data: SecondData
        private val secondTv: TextView = itemView.findViewById(R.id.second_tv)

        init {
            itemView.findViewById<RelativeLayout>(R.id.second_data_container).apply {
                setOnClickListener { listener.onClick(data) }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun bind(data: SecondData) {
            this.data = data
            secondTv.text = "${data.stringValue} click me!"
        }
    }

    interface SecondDataClickListener {
        fun onClick(secondData: SecondData)
    }
}