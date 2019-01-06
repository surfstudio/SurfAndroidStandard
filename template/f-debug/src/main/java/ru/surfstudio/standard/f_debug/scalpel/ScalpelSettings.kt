package ru.surfstudio.standard.f_debug.scalpel

import android.content.Context
import ru.surfstudio.android.shared.pref.SettingsUtil

/**
 * Настройки отображения [ScalpelFrameLayout]
 */
class ScalpelSettings(val context: Context) {
    val sharedPref = context.getSharedPreferences("ScalpelSettings", Context.MODE_PRIVATE)

    private val DRAW_IDS_KEY = "DRAW_IDS_KEY"
    private val DRAW_CLASSES_KEY = "DRAW_CLASSES_KEY"
    private val DRAW_VIEWS_CONTENT_KEY = "DRAW_VIEWS_CONTENT_KEY"


    var drawIds: Boolean
        get() = SettingsUtil.getBoolean(sharedPref, DRAW_IDS_KEY, true)
        set(value) = SettingsUtil.putBoolean(sharedPref, DRAW_IDS_KEY, value)

    var drawClasses: Boolean
        get() = SettingsUtil.getBoolean(sharedPref, DRAW_CLASSES_KEY, false)
        set(value) = SettingsUtil.putBoolean(sharedPref, DRAW_CLASSES_KEY, value)

    var drawViewsContent: Boolean
        get() = SettingsUtil.getBoolean(sharedPref, DRAW_VIEWS_CONTENT_KEY, false)
        set(value) = SettingsUtil.putBoolean(sharedPref, DRAW_VIEWS_CONTENT_KEY, value)

}