package ru.surfstudio.standard.base_ui.util

import android.content.ClipData
import android.content.ClipboardManager
import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject

/**
 * Помощник для работы с буфером обмена
 */
class ClipboardManagerHelper @Inject constructor(
        private val clipboardManager: ClipboardManager)  {

    fun copyString(str: String) {
        val clip = ClipData.newPlainText(null, str)
        clipboardManager.primaryClip = clip
    }
}