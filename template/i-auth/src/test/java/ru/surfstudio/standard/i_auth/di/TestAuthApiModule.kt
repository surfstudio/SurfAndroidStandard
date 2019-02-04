package ru.surfstudio.standard.i_auth.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.standard.i_auth.AuthApi
import ru.surfstudio.standard.i_auth.AuthRepository

@Module
class TestAuthApiModule {

    @Provides
    @PerActivity
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @PerActivity
    fun provideAuthRepository(authApi: AuthApi) = AuthRepository(authApi)
}