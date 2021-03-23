package ru.surfstudio.standard.i_network.response

import com.google.gson.annotations.SerializedName
import ru.surfstudio.standard.domain.metadata.Pagination
import ru.surfstudio.standard.i_network.network.Transformable

/**
 * Маппинг-модель метаданных [Pagination]
 */
data class PaginationObj(
        @SerializedName("totalCount") val totalCount: Int?,
        @SerializedName("pageCount") val pageCount: Int?,
        @SerializedName("currentPage") val currentPage: Int?,
        @SerializedName("perPage") val perPage: Int?
) : Transformable<Pagination> {

    override fun transform() =
            Pagination(totalCount ?: 0,
                    pageCount ?: 0,
                    currentPage ?: 0,
                    perPage ?: 0
            )
}