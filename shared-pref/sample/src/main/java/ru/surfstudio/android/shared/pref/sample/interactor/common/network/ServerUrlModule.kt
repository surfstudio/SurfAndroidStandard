package ru.surfstudio.android.shared.pref.sample.interactor.common.network

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.network.BaseUrl

@Module
class ServerUrlModule {

    companion object {
        private const val BASE_URL = "https://api.ipify.org"
    }

    @Provides
    @PerApplication
    fun provideBaseUrl(): BaseUrl = BaseUrl(BASE_URL, null)
}