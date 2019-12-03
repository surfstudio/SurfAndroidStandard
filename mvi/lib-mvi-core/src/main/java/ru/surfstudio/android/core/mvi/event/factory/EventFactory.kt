package ru.surfstudio.android.core.mvi.event.factory

/**
 * Фабрика, создающая события типа [E] на основе каких-то данных типа [T]
 */
typealias EventFactory<T, E> =  (T) -> E