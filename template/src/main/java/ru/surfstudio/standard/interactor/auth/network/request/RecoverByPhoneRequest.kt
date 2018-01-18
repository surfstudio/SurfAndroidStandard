package ru.surfstudio.standard.interactor.auth.network.request

import com.google.gson.annotations.SerializedName

/**
 * Тело запроса на получение авторизационной ссылки на телефон по SMS
 */
data class RecoverByPhoneRequest(@SerializedName("phone")
                                 private val phone: String?)