package ru.surfstudio.android.security.connection.srp

import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import java.io.Serializable

/**
 * Доменная модель данных для шифрования пароля при регистарции
 */
data class Srp6ConfigRegistration(val H: String = EMPTY_STRING,
                                  val g: Int = 0,
                                  val N: String = EMPTY_STRING) : Serializable

/**
 * Доменная модель данных, приходящих от сервера для дальнейшего шифрования
 */
data class SrpChallenge(val b: String = EMPTY_STRING,
                        val salt: String = EMPTY_STRING)