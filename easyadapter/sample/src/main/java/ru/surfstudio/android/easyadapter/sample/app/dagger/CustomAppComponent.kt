package ru.surfstudio.android.easyadapter.sample.app.dagger

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.easyadapter.sample.interactor.FirstDataRepository
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppComponent
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultSharedPrefModule
import ru.surfstudio.android.sample.dagger.app.dagger.NavigationModule

@PerApplication
@Component(
    modules = [
        DefaultAppModule::class,
        DefaultSharedPrefModule::class,
        NavigationModule::class
    ]
)
interface CustomAppComponent : DefaultAppComponent {
    fun firstDataRepository(): FirstDataRepository
}
