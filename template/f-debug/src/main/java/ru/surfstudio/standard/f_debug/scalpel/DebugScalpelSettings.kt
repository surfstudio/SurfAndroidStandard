package ru.surfstudio.standard.f_debug.scalpel

import android.content.Context
import ru.surfstudio.android.shared.pref.SettingsUtil

/**
 * Настройки отображения [DebugScalpelFrameLayout]
 */
class DebugScalpelSettings(val context: Context) {

    companion object {
        private const val DRAW_IDS_KEY = "DRAW_IDS_KEY"
        private const val DRAW_CLASSES_KEY = "DRAW_CLASSES_KEY"
        private const val DRAW_VIEWS_CONTENT_KEY = "DRAW_VIEWS_CONTENT_KEY"
    }

    private val sharedPref = context.getSharedPreferences("DebugScalpelSettings", Context.MODE_PRIVATE)

    var drawIds: Boolean
        get() = SettingsUtil.getBoolean(sharedPref, DRAW_IDS_KEY, true)
        set(value) = SettingsUtil.putBoolean(sharedPref, DRAW_IDS_KEY, value)

    var drawClasses: Boolean
        get() = SettingsUtil.getBoolean(sharedPref, DRAW_CLASSES_KEY, false)
        set(value) = SettingsUtil.putBoolean(sharedPref, DRAW_CLASSES_KEY, value)

    var drawViewsContent: Boolean
        get() = SettingsUtil.getBoolean(sharedPref, DRAW_VIEWS_CONTENT_KEY, true)
        set(value) = SettingsUtil.putBoolean(sharedPref, DRAW_VIEWS_CONTENT_KEY, value)


}