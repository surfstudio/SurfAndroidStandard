package ru.surfstudio.standard.i_network.response

import ru.surfstudio.standard.domain.metadata.Metadata
import com.google.gson.annotations.SerializedName
import ru.surfstudio.standard.i_network.network.Transformable

/**
 * Маппинг-модель метаданных [Metadata]
 */
data class MetadataObj(
        @SerializedName("totalCount") val totalCount: Int?,
        @SerializedName("pageCount") val pageCount: Int?,
        @SerializedName("currentPage") val currentPage: Int?,
        @SerializedName("perPage") val perPage: Int?
) : Transformable<Metadata> {

    override fun transform() =
            Metadata(totalCount ?: 0,
                    pageCount ?: 0,
                    currentPage ?: 0,
                    perPage ?: 0
            )
}