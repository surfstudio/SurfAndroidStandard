package ru.surfstudio.android.security.sample.app.dagger

import android.content.Context
import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultSharedPrefModule
import ru.surfstudio.android.security.sample.interactor.cache.CacheModule
import ru.surfstudio.android.security.sample.interactor.profile.ProfileInteractor
import ru.surfstudio.android.security.sample.interactor.storage.ApiKeyModule
import ru.surfstudio.android.security.sample.interactor.storage.ApiKeyStorageWrapper

@PerApplication
@Component(modules = [
    DefaultAppModule::class,
    DefaultSharedPrefModule::class,
    CacheModule::class,
    ApiKeyModule::class
])
interface CustomAppComponent {
    fun context(): Context
//    fun activeActivityHolder(): ActiveActivityHolder
//    fun connectionProvider(): ConnectionProvider
//    fun schedulerProvider(): SchedulersProvider
//    fun stringsProvider(): StringsProvider
//    fun sharedPreferences(): SharedPreferences

    fun apiKeyStorageWrapper(): ApiKeyStorageWrapper
    fun profileInteractor(): ProfileInteractor
}
