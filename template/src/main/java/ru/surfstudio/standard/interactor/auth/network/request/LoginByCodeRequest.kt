package ru.surfstudio.standard.interactor.auth.network.request

import com.google.gson.annotations.SerializedName

data class LoginByCodeRequest(@SerializedName("code")
                              val code: String? = null)
