package ru.surfstudio.android.network.sample.interactor.common.network.response

import com.google.gson.annotations.SerializedName
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.network.TransformUtil
import ru.surfstudio.android.network.Transformable
import ru.surfstudio.android.network.response.BaseResponse

/**
 * Класс, отвечающий за многостраничный ответ сервера
 */
data class PageResponse<T : Transformable<E>, E>(@SerializedName("status") val status: Int,
                                                 @SerializedName("message") val message: String?,
                                                 @SerializedName("pager") val pager: PagerObj?,
                                                 @SerializedName("result") val result: List<T>?
) : BaseResponse, Transformable<DataList<E>> {

    override fun transform(): DataList<E> =
            if (pager != null)
                DataList(
                        TransformUtil.transformCollection(result),
                        pager.currentPage,
                        pager.recordsPerPage,
                        pager.totalRecordCount,
                        pager.totalPages)
            else
                DataList.empty()
}