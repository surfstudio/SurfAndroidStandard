package ru.surfstudio.standard.interactor.auth.network.request

import com.google.gson.annotations.SerializedName

data class LoginByEmailRequest(@SerializedName("email")
                               val email: String,
                               @SerializedName("password")
                               val password: String)
