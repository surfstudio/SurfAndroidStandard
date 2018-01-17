package ru.surfstudio.standard.domain.phone

import java.io.Serializable

/**
 * Сущность для секретного кода полученного при вооде телефона и времени его жизни
 */
data class KeyInfo(val key: String? = null,
                   val expires: String? = null)
    : Serializable
