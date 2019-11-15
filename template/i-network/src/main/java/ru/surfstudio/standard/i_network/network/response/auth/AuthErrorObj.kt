package ru.surfstudio.standard.i_network.network.response.auth


import com.google.gson.annotations.SerializedName

private const val ACCESS_TOKEN_PATH = "sso/oauth2/access_token"
const val AUTH_ACCESS_TOKEN = ACCESS_TOKEN_PATH

/**
 * Пример реализации [BaseAuthErrorObj]
 */
data class AuthErrorObj(@SerializedName("errors")
                        override val error: String? = null) : BaseAuthErrorObj
