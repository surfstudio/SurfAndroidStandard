package ru.surfstudio.android.core.ui.permission.screens.settings_rational

import java.io.Serializable

/**
 * Параметры для экрана объяснения необходимости перехода в настройки приложения.
 *
 * Input params of screen for going to settings to give permissions
 *
 * @param rationalTxt rationale message
 * @param positiveButtonTxt positive button text
 * @param negativeButtonTxt negative button text
 */
data class SettingsRationalDialogParams(
    val rationalTxt: String? = null,
    val positiveButtonTxt: String? = null,
    val negativeButtonTxt: String? = null
) : Serializable

/**
 * Билдер с параметрами для экрана объяснения необходимости перехода в настройки приложения.
 *
 * Input params builder of screen for going to settings to give permissions
 *
 * @param rationalTxt rationale message
 * @param positiveButtonTxt positive button text
 * @param negativeButtonTxt negative button text
 */
data class SettingsRationalDialogParamsBuilder(
    private var rationalTxt: String? = null,
    private var positiveButtonTxt: String? = null,
    private var negativeButtonTxt: String? = null
) {

    fun rationalTxt(rationalTxt: String) = apply {
        this.rationalTxt = rationalTxt
    }

    fun positiveButtonTxt(positiveButtonTxt: String) = apply {
        this.positiveButtonTxt = positiveButtonTxt
    }

    fun negativeButtonTxt(negativeButtonTxt: String) = apply {
        this.negativeButtonTxt = negativeButtonTxt
    }

    fun build(): SettingsRationalDialogParams =
        SettingsRationalDialogParams(rationalTxt, positiveButtonTxt, negativeButtonTxt)
}