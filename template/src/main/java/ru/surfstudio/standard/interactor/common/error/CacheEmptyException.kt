package ru.surfstudio.standard.interactor.common.error

/**
 * внутренне исключение для механизма работы с сервером, используется для возвращения null в случае
 * попытки получения кеша, которого не существует
 */
class CacheEmptyException : NetworkException()
