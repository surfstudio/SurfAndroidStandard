package ru.surfstudio.standard.interactor.auth

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import ru.surfstudio.standard.interactor.auth.network.AuthApi

@Module
class AuthModule {

    @Provides
    @PerApplication
    internal fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

}