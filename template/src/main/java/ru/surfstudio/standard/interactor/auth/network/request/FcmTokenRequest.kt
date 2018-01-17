package ru.surfstudio.standard.interactor.auth.network.request

import com.google.gson.annotations.SerializedName

/**
 * Запрос на токен fcm
 */
data class FcmTokenRequest(@SerializedName("deviceId")
                           private val deviceId: String? = null)
