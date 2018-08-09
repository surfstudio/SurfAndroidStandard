package ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.easyadapter.sample.R
import ru.surfstudio.android.easyadapter.sample.domain.FirstData

class FirstDataItemController(private val listener: FirstDataClickListener
) : BindableItemController<FirstData, FirstDataItemController.Holder>() {

    override fun getItemId(data: FirstData): Long = data.hashCode().toLong()

    override fun createViewHolder(parent: ViewGroup?): Holder = Holder(listener, parent)

    inner class Holder(private val listener: FirstDataClickListener,
                       parent: ViewGroup?
    ) : BindableViewHolder<FirstData>(parent, R.layout.first_data_item_controller) {

        private lateinit var data: FirstData
        private val firstTv: TextView = itemView.findViewById(R.id.first_tv)

        init {
            itemView.findViewById<RelativeLayout>(R.id.first_data_container).apply {
                setOnClickListener { listener.onClick(data) }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun bind(data: FirstData) {
            this.data = data
            firstTv.text = "$data click me!"
        }
    }

    interface FirstDataClickListener {
        fun onClick(firstData: FirstData)
    }
}