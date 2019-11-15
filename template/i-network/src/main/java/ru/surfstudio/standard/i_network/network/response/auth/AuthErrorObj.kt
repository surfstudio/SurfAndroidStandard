package ru.surfstudio.standard.i_network.network.response.auth


import com.google.gson.annotations.SerializedName

/**
 * Пример реализации [BaseAuthErrorObj]
 */
data class AuthErrorObj(@SerializedName("errors")
                        override val error: String? = null) : BaseAuthErrorObj
