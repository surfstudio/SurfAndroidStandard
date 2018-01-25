package ru.surfstudio.standard.interactor.common.network.error;


import com.annimon.stream.Stream;

/**
 * коды ошибок {@link }
 */
public enum ApiErrorCode {
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

    private String value;

    ApiErrorCode(String value) {
        this.value = value;
    }

    public static ApiErrorCode getByValue(String value) {
        return Stream.of(values())
                .filter(error -> error.value.equalsIgnoreCase(value))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
