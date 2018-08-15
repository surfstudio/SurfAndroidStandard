package ru.surfstudio.android.firebase.sample.domain.notification

/**
 * Типы уведомлений
 */
enum class NotificationType(val id: Int) {
    FIRST_TYPE(0),
    SECOND_TYPE(1),
    NO_DATA_TYPE(2),
    NO_ACTION_AND_DATA_TYPE(3),
    UNKNOWN(-1);

    companion object {
        fun getById(id: Int?) = values().firstOrNull { it.id == id } ?: UNKNOWN
    }

    fun getStringId(): String = id.toString()
}