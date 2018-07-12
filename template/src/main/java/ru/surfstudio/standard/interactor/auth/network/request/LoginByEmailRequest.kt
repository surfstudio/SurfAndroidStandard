package ru.surfstudio.standard.interactor.auth.network.request

import com.google.gson.annotations.SerializedName

//todo Обновить или удалить класс в соответствии с нуждами приложения
data class LoginByEmailRequest(@SerializedName("email")
                               val email: String,
                               @SerializedName("password")
                               val password: String)
