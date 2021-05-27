package ru.surfstudio.standard.i_network.error

/**
 * типы специфичных ошибок сервера
 *
 * @param code код ошибки
 */
enum class ApiErrorType(private val code: Int) {
    UNKNOWN(-1);

    companion object {

        fun getBy(code: Int?): ApiErrorType =
                values().firstOrNull { error -> error.code == code } ?: UNKNOWN
    }
}
