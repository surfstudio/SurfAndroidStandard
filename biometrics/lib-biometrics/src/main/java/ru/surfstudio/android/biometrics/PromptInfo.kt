package ru.surfstudio.android.biometrics

/**
 * Info to show biometric dialog.
 *
 * @param title title of the dialog
 * @param subtitle subtitle of the dialog
 * @param negativeButtonText text of the negative button on the dialog
 */
data class PromptInfo(
    val title: String,
    val subtitle: String,
    val negativeButtonText: String
)