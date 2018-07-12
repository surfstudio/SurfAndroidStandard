package ru.surfstudio.standard.interactor.auth.network.request

import com.google.gson.annotations.SerializedName

/**
 * Тело запроса на получение авторизационной ссылки на e-mail
 */
//todo Обновить или удалить класс в соответствии с нуждами приложения
data class RecoverByEmailRequest(@SerializedName("email")
                                 private val email: String?)
