package ru.surfstudio.standard.interactor.auth.network.request

import com.google.gson.annotations.SerializedName

//todo Обновить или удалить класс в соответствии с нуждами приложения
data class LoginByCodeRequest(@SerializedName("code")
                              val code: String? = null)
