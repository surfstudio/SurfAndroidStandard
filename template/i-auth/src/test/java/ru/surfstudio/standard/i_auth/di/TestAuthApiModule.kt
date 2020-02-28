package ru.surfstudio.standard.i_auth.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.surfstudio.standard.i_auth.AuthApi
import ru.surfstudio.standard.i_auth.AuthRepository
import ru.surfstudio.standard.small_test_utils.di.scope.PerTest

@Module
class TestAuthApiModule {

    @Provides
    @PerTest
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @PerTest
    fun provideAuthRepository(authApi: AuthApi) = AuthRepository(authApi)
}