package ru.surfstudio.android.core.mvi.event.factory

/**
 * Фабрика, создающая события типа [E] без каких-либо параметров
 */
typealias ParamlessEventFactory<E> = () -> E