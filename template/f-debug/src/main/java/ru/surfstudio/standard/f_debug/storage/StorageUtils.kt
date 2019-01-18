package ru.surfstudio.standard.f_debug.storage

import android.annotation.SuppressLint
import android.content.SharedPreferences

/**
 * Используется для синхронного сохранения данных в SharedPreferences
 */
@SuppressLint("ApplySharedPref")
fun putBoolean(sp: SharedPreferences, key: String, value: Boolean) {
    val editor = sp.edit()
    editor.putBoolean(key, value)
    editor.commit()
}

/**
 * Используется для синхронного сохранения данных в SharedPreferences
 */
@SuppressLint("ApplySharedPref")
fun putLong(sp: SharedPreferences, key: String, value: Long) {
    val editor = sp.edit()
    editor.putLong(key, value)
    editor.commit()
}