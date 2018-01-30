package ru.surfstudio.standard.interactor.common.network.response

import com.google.gson.annotations.SerializedName

/**
 * Стандартный объект ошибки
 */

class ErrorObj(@SerializedName("code")
               val code: String? = null,
               @SerializedName("body")
               val message: String? = null)
