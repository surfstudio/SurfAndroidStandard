package ru.surfstudio.standard.domain.auth.recover

/**
 * Статус восстановления доступа через email
 */
data class RecoverByEmailStatus(val email: String) : RecoverStatus()
