package ru.surfstudio.standard.interactor.auth.network.request

import com.google.gson.annotations.SerializedName

/**
 * сущность для запроса на получение ключа по номеру телефона
 */
data class LoginByPhoneRequest(@SerializedName("phone")
                               private val phone: String? = null)
