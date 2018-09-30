package ru.surfstudio.android.network.sample.interactor.product

import io.reactivex.Observable
import org.junit.Test
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.network.sample.domain.common.QueryConstants
import ru.surfstudio.android.network.sample.domain.product.Product
import ru.surfstudio.android.network.sample.util.BaseDaggerTest
import ru.surfstudio.android.network.sample.util.TestNetworkAppComponent
import javax.inject.Inject

class ProductRepositoryTest : BaseDaggerTest() {

    @Inject
    lateinit var productRepository: ProductRepository

    override fun inject(networkComponent: TestNetworkAppComponent) {
        networkComponent.inject(this)
    }

    @Test
    fun getProductsTest() {
        test(getFirstPage())
    }

    @Test
    fun getProductsCountTest() {
        assert(testAndGetFirstValue(getFirstPage()).size == QueryConstants.QUERY_PER_PAGE_VALUE)
    }

    private fun getFirstPage(): Observable<DataList<Product>> = productRepository.getProducts(1)
}