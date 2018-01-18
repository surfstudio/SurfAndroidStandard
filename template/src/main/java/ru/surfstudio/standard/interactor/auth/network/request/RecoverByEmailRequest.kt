package ru.surfstudio.standard.interactor.auth.network.request

import com.google.gson.annotations.SerializedName

/**
 * Тело запроса на получение авторизационной ссылки на e-mail
 */
data class RecoverByEmailRequest(@SerializedName("email")
                                 private val email: String?)
