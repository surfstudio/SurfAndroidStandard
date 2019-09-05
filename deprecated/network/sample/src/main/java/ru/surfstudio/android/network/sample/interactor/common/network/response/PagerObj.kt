package ru.surfstudio.android.network.sample.interactor.common.network.response

import com.google.gson.annotations.SerializedName

/**
 * Класс, отвечающий за хранение вспомогательной информации о текущей странице данных
 */
data class PagerObj(@SerializedName("records_per_page") val recordsPerPage: Int,
                    @SerializedName("total_record_count") val totalRecordCount: Int,
                    @SerializedName("current_page_record_count") val currentPageRecordCount: Int,
                    @SerializedName("is_first_page") val isFirstPage: Boolean,
                    @SerializedName("is_final_page") val isFinalPage: Boolean,
                    @SerializedName("current_page") val currentPage: Int,
                    @SerializedName("next_page") val nextPage: Int,
                    @SerializedName("previous_page") val previousPage: Int?,
                    @SerializedName("total_pages") val totalPages: Int)