package ru.surfstudio.standard.domain.notification

/**
 * Типы уведомлений
 */
enum class NotificationType(val id: Int) {
    DEBUG_DATA_TYPE(1),
    UNKNOWN(-1);

    companion object {
        fun getById(id: Int?) = values().firstOrNull { it.id == id } ?: UNKNOWN
    }

    fun getStringId(): String = id.toString()
}