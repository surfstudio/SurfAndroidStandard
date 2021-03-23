package ru.surfstudio.standard.ui.util.back_press

import androidx.activity.OnBackPressedCallback

/**
 * Реализация [OnBackPressedCallback] по-умолчанию. Колбек сразу же при создании становится активным.
 *
 * @param onBackPressed действие, выполняемое при получении события onBackPressed родительской Activity
 */
class DefaultBackPressedCallback(private val onBackPressed: () -> Unit) : OnBackPressedCallback(true) {

    override fun handleOnBackPressed() {
        onBackPressed()
    }
}
