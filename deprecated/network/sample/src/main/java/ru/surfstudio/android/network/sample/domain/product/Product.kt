package ru.surfstudio.android.network.sample.domain.product

data class Product(val id: Int,
                   val isDead: Boolean,
                   val name: String,
                   val tags: String,
                   val priceInCents: Int,
                   val regularPriceInCents: Int,
                   val stockType: String,
                   val primaryCategory: String,
                   val secondaryCategory: String,
                   val origin: String,
                   val volumeInMilliliters: Int,
                   val alcoholContent: Int,
                   val pricePerLiterInCents: Int,
                   val producerName: String,
                   val isSeasonal: Boolean)