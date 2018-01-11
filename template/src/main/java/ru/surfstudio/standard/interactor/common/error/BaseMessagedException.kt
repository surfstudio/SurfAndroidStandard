package ru.surfstudio.standard.interactor.common.error


/**
 * Базовый класс ошибки
 */

abstract class BaseMessagedException protected constructor(val developerMessage: String) : RuntimeException()
