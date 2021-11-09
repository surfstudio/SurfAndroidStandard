package ru.surfstudio.standard.i_network.generated.repo

import io.reactivex.Completable
import io.reactivex.Single
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.i_network.network.transform
import ru.surfstudio.standard.domain.entity.KeyInfoEntity
import ru.surfstudio.standard.domain.entity.LoginInfoEntity
import ru.surfstudio.standard.i_network.generated.api.AuthApi
import ru.surfstudio.standard.i_network.generated.entry.LoginByCodeRequestEntry
import ru.surfstudio.standard.i_network.generated.entry.LoginByPhoneRequestEntry
import ru.surfstudio.standard.i_network.service.BaseNetworkService
import javax.inject.Inject

private const val CODE_FORMAT = "%s:%s"

/**
 * Сервис, отвечающий за авторизацию и регистрацию пользователя
 */
@PerApplication
class AuthRepository @Inject constructor(
        private val authApi: AuthApi
) : BaseNetworkService() {

    /**
     * Отсылка номера телефона для получения кода авторизации
     */
    fun requestCode(phoneNumber: String): Single<KeyInfoEntity> =
            authApi.requestCode(LoginByPhoneRequestEntry(phoneNumber))
                    .transform()

    /**
     * Вход по полученному из смс коду
     */
    fun loginByCode(key: String, smsCode: String): Single<LoginInfoEntity> {
        val code = String.format(CODE_FORMAT, key, smsCode)
        return authApi.loginByCode(LoginByCodeRequestEntry(code))
                .transform()
    }

    /**
     * Выход текущего авторизованного пользователя
     */
    fun logout(): Completable = authApi.logout()
}