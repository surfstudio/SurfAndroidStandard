package ru.surfstudio.android.utilktx.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import java.util.HashSet

/**
 * хелпер для работы с SharedPref
 */
object SettingsUtil {
    val EMPTY_STRING_SETTING = ""
    val EMPTY_INT_SETTING = -1
    val EMPTY_LONG_SETTING = -1L

    fun getString(context: Context, key: String): String {
        return getString(getDefaultSharedPreferences(context), key)
    }

    fun putString(context: Context, key: String, value: String) {
        putString(getDefaultSharedPreferences(context), key, value)
    }

    fun putInt(context: Context, key: String, value: Int) {
        putInt(getDefaultSharedPreferences(context), key, value)
    }

    fun putLong(context: Context, key: String, value: Long) {
        putLong(getDefaultSharedPreferences(context), key, value)
    }

    fun getInt(context: Context, key: String): Int {
        return getInt(getDefaultSharedPreferences(context), key)
    }

    fun getLong(context: Context, key: String): Long {
        return getLong(getDefaultSharedPreferences(context), key)
    }

    fun putBoolean(context: Context, key: String, value: Boolean) {
        putBoolean(getDefaultSharedPreferences(context), key, value)
    }

    fun getBoolean(context: Context, key: String, defaultValue: Boolean): Boolean {
        return getBoolean(getDefaultSharedPreferences(context), key, defaultValue)
    }

    fun getString(sp: SharedPreferences, key: String): String {
        return sp.getString(key, EMPTY_STRING_SETTING)
    }

    fun getStringSet(sp: SharedPreferences, key: String): Set<String> {
        return sp.getStringSet(key, HashSet())
    }

    fun putStringSet(sp: SharedPreferences, key: String, value: Set<String>) {
        val editor = sp.edit()
        editor.putStringSet(key, value)
        saveChanges(editor)
    }

    fun putString(sp: SharedPreferences, key: String, value: String) {
        val editor = sp.edit()
        editor.putString(key, value)
        saveChanges(editor)
    }

    fun putInt(sp: SharedPreferences, key: String, value: Int) {
        val editor = sp.edit()
        editor.putInt(key, value)
        saveChanges(editor)
    }

    fun putLong(sp: SharedPreferences, key: String, value: Long) {
        val editor = sp.edit()
        editor.putLong(key, value)
        saveChanges(editor)
    }

    fun getInt(sp: SharedPreferences, key: String): Int {
        return sp.getInt(key, EMPTY_INT_SETTING)
    }

    fun getLong(sp: SharedPreferences, key: String): Long {
        return sp.getLong(key, EMPTY_LONG_SETTING)
    }

    fun putBoolean(sp: SharedPreferences, key: String, value: Boolean) {
        val editor = sp.edit()
        editor.putBoolean(key, value)
        saveChanges(editor)
    }

    fun getBoolean(sp: SharedPreferences, key: String, defaultValue: Boolean): Boolean {
        return sp.getBoolean(key, defaultValue)
    }

    private fun saveChanges(editor: SharedPreferences.Editor) {
        editor.apply()
    }

    private fun getDefaultSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

}//do nothing
