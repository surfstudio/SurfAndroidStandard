package ru.surfstudio.android.security.sample.app.dagger

import dagger.Component
import ru.surfstudio.android.biometrics.BiometricsService
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppComponent
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultSharedPrefModule
import ru.surfstudio.android.sample.dagger.app.dagger.NavigationModule
import ru.surfstudio.android.security.sample.interactor.biometrics.BiometricsModule
import ru.surfstudio.android.security.sample.interactor.cache.CacheModule
import ru.surfstudio.android.security.sample.interactor.profile.ProfileInteractor
import ru.surfstudio.android.security.sample.interactor.session.SessionModule
import ru.surfstudio.android.security.sample.interactor.storage.ApiKeyModule
import ru.surfstudio.android.security.sample.interactor.storage.ApiKeyStorageWrapper
import ru.surfstudio.android.security.session.SessionActivityCallback

@PerApplication
@Component(
    modules = [
        DefaultAppModule::class,
        DefaultSharedPrefModule::class,
        NavigationModule::class,
        CacheModule::class,
        ApiKeyModule::class,
        SessionModule::class,
        BiometricsModule::class
    ]
)
interface CustomAppComponent : DefaultAppComponent {
    fun apiKeyStorageWrapper(): ApiKeyStorageWrapper
    fun profileInteractor(): ProfileInteractor
    fun sessionActivityCallback(): SessionActivityCallback
    fun biometricsService(): BiometricsService
}
