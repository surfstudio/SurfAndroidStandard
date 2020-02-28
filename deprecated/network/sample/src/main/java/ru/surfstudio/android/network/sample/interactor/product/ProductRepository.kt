package ru.surfstudio.android.network.sample.interactor.product

import io.reactivex.Observable
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.network.BaseNetworkInteractor
import ru.surfstudio.android.network.sample.domain.product.Product
import ru.surfstudio.android.network.sample.interactor.product.network.ProductApi
import ru.surfstudio.android.network.transform
import javax.inject.Inject


/**
 * Репозиторий для товаров
 */
@PerApplication
class ProductRepository @Inject constructor(connectionQualityProvider: ConnectionProvider,
                                            private val productApi: ProductApi
) : BaseNetworkInteractor(connectionQualityProvider) {

    fun getProducts(page: Int): Observable<DataList<Product>> =
            hybridQueryWithSimpleCache { queryMode ->
                productApi.getProducts(queryMode, page)
            }.transform()
}