package ru.surfstudio.standard.domain.auth.phone

import java.io.Serializable

/**
 * Сущность для секретного кода полученного при вводе телефона и времени его жизни
 */
data class KeyInfo(val key: String? = null,
                   val expires: String? = null)
    : Serializable
