package ru.surfstudio.standard.i_network.generated.di

import ru.surfstudio.standard.i_network.generated.api.AuthApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.surfstudio.android.dagger.scope.PerApplication

@Module
class AuthModule {

    @Provides
    @PerApplication
    internal fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}