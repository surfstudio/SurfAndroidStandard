package ru.surfstudio.android.core.ui.event.back

/**
 * Интерфейс для фрагментов, что бы активити понимала, что данный фрагмент обрабатывает нажатие назад
 */
interface OnBackPressed {
    fun onBackPressed(): Boolean
}