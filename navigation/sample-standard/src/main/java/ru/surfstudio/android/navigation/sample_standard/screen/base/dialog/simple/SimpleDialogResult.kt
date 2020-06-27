package ru.surfstudio.android.navigation.sample_standard.screen.base.dialog.simple

import java.io.Serializable

/**
 * Результат работы простого диалога.
 *
 * @property POSITIVE нажата кнопка ОК
 * @property NEGATIVE нажата кнопка Отмены
 * @property DISMISS диалог отменен (по беку или по тапу за границу диалога)
 */
enum class SimpleDialogResult : Serializable {
    POSITIVE,
    NEGATIVE,
    DISMISS
}
