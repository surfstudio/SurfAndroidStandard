package ru.surfstudio.standard.i_network.error

/**
 * Примеры кодов специфичных ошибок сервера
 */
enum class ApiErrorCode(val code: Int) {

    // TODO дописать сюда остальные из HttpCodes, его удалить

    NOT_AUTHORIZED(401),
    FORBIDDEN(403),
    NOT_MODIFIED(304),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500),
    UNKNOWN(-1);

    companion object {
        fun from(code: Int): ApiErrorCode  =
                values().firstOrNull { error -> error.code == code } ?: UNKNOWN
    }
}