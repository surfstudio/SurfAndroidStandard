package ru.surfstudio.standard.domain.login

import java.io.Serializable

/**
 * Сущность для секретного кода полученного при вводе телефона и времени его жизни
 */
//todo Обновить или удалить класс в соответствии с нуждами приложения
data class KeyInfo(val key: String? = null,
                   val expires: String? = null) : Serializable