package ru.surfstudio.android.core.mvi.ui.holder

/**
 * Лиснер, срабатывающий при изменения State типа [S]
 */
typealias StateChangedListener<S> = (S) -> Unit