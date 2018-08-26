package ru.surfstudio.android.network.sample.interactor.product

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.network.sample.interactor.product.network.ProductApi

@Module
class ProductModule {

    @Provides
    @PerApplication
    fun provideProductApi(retrofit: Retrofit): ProductApi {
        return retrofit.create(ProductApi::class.java)
    }
}