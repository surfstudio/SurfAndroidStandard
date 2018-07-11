package ru.surfstudio.standard.interactor.common.network.error


/**
 * коды ошибок []
 */
enum class ApiErrorCode private constructor(private val value: String) {
    /* todo определить специфичные для проекта ошибки api
    USER_BLOCKED("UserBlocked"),
    PHONE_INVALID("PhoneInvalid"),
    VERIFICATION_CODE_ALREADY_SENT("VerificationCodeAlreadySent"),
    VERIFICATION_CODE_INVALID("VerificationCodeInvalid"),
    SMS_ERROR("invalid_grant"),
    TOKEN_EXPIRED("TokenExpired"),
    INTERNAL_ERROR("InternalError"),
    NOT_FOUND("NotFound"), */

    UNKNOWN("");


    companion object {

        fun getByValue(value: String): ApiErrorCode {
            return values()
                    .firstOrNull { error -> error.value.equals(value, true) }
                    ?: UNKNOWN
        }
    }
}
