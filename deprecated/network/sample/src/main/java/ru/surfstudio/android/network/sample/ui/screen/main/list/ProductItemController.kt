package ru.surfstudio.android.network.sample.ui.screen.main.list

import android.view.ViewGroup
import android.widget.TextView
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.network.sample.R
import ru.surfstudio.android.network.sample.domain.product.Product

class ProductItemController(
        private val onClickListener: (Product) -> Unit
): BindableItemController<Product, ProductItemController.Holder>() {

    override fun getItemId(data: Product): String = data.id.toString()

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    inner class Holder(
            parent: ViewGroup
    ): BindableViewHolder<Product>(parent, R.layout.product_item) {

        private val nameTv: TextView = itemView.findViewById(R.id.name_tv)
        private val priceTv: TextView = itemView.findViewById(R.id.price_tv)

        private lateinit var product: Product

        init {
            itemView.setOnClickListener { onClickListener(product) }
        }
        override fun bind(data: Product) {
            product = data
            nameTv.text = data.name
            priceTv.text = data.priceInCents.toString()
        }
    }
}