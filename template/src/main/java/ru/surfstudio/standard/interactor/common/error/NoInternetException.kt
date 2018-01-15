package ru.surfstudio.standard.interactor.common.error

/**
 * отсутствует подключение к интернету
 */
class NoInternetException(e: Throwable) : NetworkException(e)
