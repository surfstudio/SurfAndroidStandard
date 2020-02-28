package ru.surfstudio.android.network.sample.interactor.product.network.response

import com.google.gson.annotations.SerializedName
import ru.surfstudio.android.network.Transformable
import ru.surfstudio.android.network.sample.domain.product.Product

data class ProductResponse(@SerializedName("id") val id: Int,
                           @SerializedName("is_dead") val isDead: Boolean,
                           @SerializedName("name") val name: String,
                           @SerializedName("tags") val tags: String,
                           @SerializedName("price_in_cents") val priceInCents: Int,
                           @SerializedName("regular_price_in_cents") val regularPriceInCents: Int,
                           @SerializedName("stock_type") val stockType: String,
                           @SerializedName("primary_category") val primaryCategory: String,
                           @SerializedName("secondary_category") val secondaryCategory: String,
                           @SerializedName("origin") val origin: String,
                           @SerializedName("volume_in_milliliters") val volumeInMilliliters: Int,
                           @SerializedName("alcohol_content") val alcoholContent: Int,
                           @SerializedName("price_per_liter_in_cents") val pricePerLiterInCents: Int,
                           @SerializedName("producer_name") val producerName: String,
                           @SerializedName("is_seasonal") val isSeasonal: Boolean
) : Transformable<Product> {
    override fun transform() = Product(
            id,
            isDead,
            name,
            tags,
            priceInCents,
            regularPriceInCents,
            stockType,
            primaryCategory,
            secondaryCategory,
            origin,
            volumeInMilliliters,
            alcoholContent,
            pricePerLiterInCents,
            producerName,
            isSeasonal
    )
}