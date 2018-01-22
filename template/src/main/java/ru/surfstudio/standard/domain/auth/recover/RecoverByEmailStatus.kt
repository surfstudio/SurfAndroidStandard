package ru.surfstudio.standard.domain.auth.recover

/**
 * Статус восстановления доступа через email
 */
//todo Обновить или удалить класс в соответствии с нуждами приложения
data class RecoverByEmailStatus(val email: String) : RecoverStatus()
