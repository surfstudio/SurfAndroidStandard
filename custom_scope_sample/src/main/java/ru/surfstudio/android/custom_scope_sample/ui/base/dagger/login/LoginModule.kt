package ru.surfstudio.android.custom_scope_sample.ui.base.dagger.login

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.scope.PerLogin

@Module
class LoginModule {
    val loginData: LoginData = LoginData()

    @PerLogin
    @Provides
    fun provideLoginData(): LoginData = loginData
}