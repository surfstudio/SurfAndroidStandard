package ru.surfstudio.android.network.sample.ui.base.dagger.activity

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.network.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.network.sample.interactor.product.ProductRepository
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule

/**
 * Компонент для @PerActivity скоупа
 */

@PerActivity
@Component(dependencies = [(CustomAppComponent::class)],
        modules = [(DefaultActivityModule::class)])
interface CustomActivityComponent : DefaultActivityComponent {
    fun productRepository(): ProductRepository
}