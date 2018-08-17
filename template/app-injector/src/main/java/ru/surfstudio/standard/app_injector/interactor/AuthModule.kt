package ru.surfstudio.standard.app_injector.interactor

import ru.surfstudio.standrad.i_auth.AuthApi
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