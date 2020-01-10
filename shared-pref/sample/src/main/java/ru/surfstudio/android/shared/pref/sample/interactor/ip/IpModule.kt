package ru.surfstudio.android.shared.pref.sample.interactor.ip

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.shared.pref.sample.interactor.ip.network.IpApi

@Module
class IpModule {

    @Provides
    @PerApplication
    fun provideIpApi(retrofit: Retrofit): IpApi = retrofit.create(IpApi::class.java)
}