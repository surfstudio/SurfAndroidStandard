package ru.surfstudio.standard.domain.notification

/**
 * Типы уведомлений
 * todo изменить исходя из нужд приложения
 */
enum class NotificationType(val id: Int) {

    UNKNOWN(-1);

    companion object {
        fun getById(id: Int?) = values().firstOrNull { it.id == id } ?: UNKNOWN
    }
}