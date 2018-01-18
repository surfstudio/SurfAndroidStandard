package ru.surfstudio.standard.domain.auth.recover

/**
 * Статус восстановления доступа через телефон
 */

data class RecoverByPhoneStatus(val phone: String) : RecoverStatus()