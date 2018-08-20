package ru.surfstudio.android.network.sample.interactor.product.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.surfstudio.android.network.BaseServerConstants
import ru.surfstudio.android.network.BaseServerConstants.HEADER_QUERY_MODE
import ru.surfstudio.android.network.sample.domain.common.QueryConstants.QUERY_PAGE
import ru.surfstudio.android.network.sample.domain.common.QueryConstants.QUERY_PER_PAGE_NAME
import ru.surfstudio.android.network.sample.domain.common.QueryConstants.QUERY_PER_PAGE_VALUE
import ru.surfstudio.android.network.sample.domain.product.Product
import ru.surfstudio.android.network.sample.interactor.common.network.ServerUrls.GET_PRODUCTS_URL
import ru.surfstudio.android.network.sample.interactor.common.network.response.PageResponse
import ru.surfstudio.android.network.sample.interactor.product.network.response.ProductResponse

/**
 * Api для получения информации о товарах
 */
interface ProductApi {

    @GET(GET_PRODUCTS_URL)
    fun getProducts(@Header(HEADER_QUERY_MODE) @BaseServerConstants.QueryMode queryMode: Int,
                    @Query(QUERY_PAGE) page: Int,
                    @Query(QUERY_PER_PAGE_NAME) perPage: Int = QUERY_PER_PAGE_VALUE)
            : Observable<PageResponse<ProductResponse, Product>>
}