package ru.surfstudio.standard.interactor.common.error

/**
 * ошибки сервиса (приходят в теле ответа)
 */
class ApiServiceException(val errorCode: Int, val userMessage: String) : NetworkException() {

    override fun toString(): String {
        return "ApiServiceException{" +
                "errorCode=" + errorCode +
                ", userMessage='" + userMessage + '\'' +
                '}'
    }
}
