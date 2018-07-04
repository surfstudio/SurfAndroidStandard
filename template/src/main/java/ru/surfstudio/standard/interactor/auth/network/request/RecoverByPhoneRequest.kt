package ru.surfstudio.standard.interactor.auth.network.request

import com.google.gson.annotations.SerializedName

/**
 * Тело запроса на получение авторизационной ссылки на телефон по SMS
 */
//todo Обновить или удалить класс в соответствии с нуждами приложения
data class RecoverByPhoneRequest(@SerializedName("phone")
                                 private val phone: String?)