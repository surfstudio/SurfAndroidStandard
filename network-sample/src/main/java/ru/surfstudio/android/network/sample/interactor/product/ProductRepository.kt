package ru.surfstudio.android.network.sample.interactor.product

import io.reactivex.Observable
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.network.BaseNetworkInteractor
import ru.surfstudio.android.network.TransformUtil
import ru.surfstudio.android.network.sample.domain.product.Product
import ru.surfstudio.android.network.sample.interactor.product.network.ProductApi
import javax.inject.Inject

/**
 * Репозиторий для товаров
 */
@PerApplication
class ProductRepository @Inject constructor(connectionQualityProvider: ConnectionProvider,
                                            private val productApi: ProductApi
) : BaseNetworkInteractor(connectionQualityProvider) {

    fun getProducts(page: Int): Observable<List<Product>> {
        return hybridQueryWithSimpleCache { queryMode ->
            productApi.getProducts(queryMode, page)
        }.map {
            TransformUtil.transformCollection(it.result)
        }
    }
}