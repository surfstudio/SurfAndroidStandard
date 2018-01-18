package ru.surfstudio.standard.domain.auth.phone


/**
 * сущность, представляющая информацию о токене и его времени жизни
 */
data class LoginInfo(val accessToken: String? = null,
                     val expiresIn: Int = 0,
                     val tokenType: String? = null,
                     val refreshToken: String? = null)
