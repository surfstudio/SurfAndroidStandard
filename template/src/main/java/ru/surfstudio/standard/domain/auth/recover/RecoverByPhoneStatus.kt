package ru.surfstudio.standard.domain.auth.recover

/**
 * Статус восстановления доступа через телефон
 */
//todo Обновить или удалить класс в соответствии с нуждами приложения
data class RecoverByPhoneStatus(val phone: String) : RecoverStatus()