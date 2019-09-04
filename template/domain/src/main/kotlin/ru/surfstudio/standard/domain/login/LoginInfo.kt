package ru.surfstudio.standard.domain.login

/**
 * сущность, представляющая информацию о токене и его времени жизни
 */
//todo Обновить или удалить класс в соответствии с нуждами приложения
data class LoginInfo(val accessToken: String,
                     val expiresIn: Int = 0,
                     val tokenType: String,
                     val refreshToken: String? = null,
                     val userId: Long = 0L)