package ru.surfstudio.standard.i_network.generated.entry

import com.google.gson.annotations.SerializedName
import ru.surfstudio.standard.domain.entity.PaginationEntity
import ru.surfstudio.standard.i_network.network.Transformable

/**
 * Маппинг-модель метаданных [PaginationEntity]
 */
data class PaginationEntry(
        @SerializedName("totalCount") val totalCount: Int?,
        @SerializedName("pageCount") val pageCount: Int?,
        @SerializedName("currentPage") val currentPage: Int?,
        @SerializedName("perPage") val perPage: Int?
) : Transformable<PaginationEntity> {

    override fun transform() =
            PaginationEntity(totalCount ?: 0,
                    pageCount ?: 0,
                    currentPage ?: 0,
                    perPage ?: 0
            )
}