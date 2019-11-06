/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.shared.pref

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import java.util.*

/**
 * хелпер для работы с SharedPref
 */
const val NO_BACKUP_SHARED_PREF = "NO_BACKUP_SHARED_PREF"
const val BACKUP_SHARED_PREF = "BACKUP_SHARED_PREF"

object SettingsUtil {

    const val EMPTY_STRING_SETTING = ""
    const val EMPTY_INT_SETTING = -1
    const val EMPTY_LONG_SETTING = -1L

    private val sharedPreferencesEditorMap = mutableMapOf<SharedPreferences, SharedPreferences.Editor>()

    fun getString(context: Context,
                  key: String,
                  prefName: String = EMPTY_STRING): String {
        return getString(getSharedPreferences(context, prefName), key)
    }

    fun putString(context: Context,
                  key: String,
                  value: String,
                  prefName: String = EMPTY_STRING) {
        putString(getSharedPreferences(context, prefName), key, value)
    }

    fun putInt(context: Context,
               key: String,
               value: Int,
               prefName: String = EMPTY_STRING) {
        putInt(getSharedPreferences(context, prefName), key, value)
    }

    fun putLong(context: Context,
                key: String,
                value: Long,
                prefName: String = EMPTY_STRING) {
        putLong(getSharedPreferences(context, prefName), key, value)
    }

    fun getInt(context: Context,
               key: String,
               prefName: String = EMPTY_STRING): Int {
        return getInt(getSharedPreferences(context, prefName), key)
    }

    fun getLong(context: Context,
                key: String,
                prefName: String = EMPTY_STRING): Long {
        return getLong(getSharedPreferences(context, prefName), key)
    }

    fun putBoolean(context: Context,
                   key: String,
                   value: Boolean,
                   prefName: String = EMPTY_STRING) {
        putBoolean(getSharedPreferences(context, prefName), key, value)
    }

    fun removeKey(context: Context,
                  key: String,
                  prefName: String = EMPTY_STRING) {
        removeKey(getSharedPreferences(context, prefName), key)
    }

    fun clear(context: Context, prefName: String = EMPTY_STRING) {
        clear(getSharedPreferences(context, prefName))
    }

    fun getBoolean(context: Context,
                   key: String,
                   defaultValue: Boolean,
                   prefName: String = EMPTY_STRING): Boolean {
        return getBoolean(getSharedPreferences(context, prefName), key, defaultValue)
    }

    fun getString(sp: SharedPreferences, key: String): String {
        return sp.getString(key, EMPTY_STRING_SETTING) ?: EMPTY_STRING
    }

    fun getStringSet(sp: SharedPreferences, key: String): Set<String> {
        return sp.getStringSet(key, HashSet()) ?: setOf()
    }

    fun getBoolean(sp: SharedPreferences, key: String, defaultValue: Boolean): Boolean {
        return sp.getBoolean(key, defaultValue)
    }

    fun getInt(sp: SharedPreferences, key: String): Int {
        return sp.getInt(key, EMPTY_INT_SETTING)
    }

    fun getLong(sp: SharedPreferences, key: String): Long {
        return sp.getLong(key, EMPTY_LONG_SETTING)
    }

    fun getLong(sp: SharedPreferences,
                key: String,
                defaultValue: Long): Long {
        return sp.getLong(key, defaultValue)
    }

    fun putStringSet(sp: SharedPreferences,
                     key: String,
                     value: Set<String>) {
        val editor = getOrCreateEditor(sp)
        editor.putStringSet(key, value)
        saveChanges(editor)
    }

    fun putString(sp: SharedPreferences,
                  key: String,
                  value: String) {
        val editor = getOrCreateEditor(sp)
        editor.putString(key, value)
        saveChanges(editor)
    }

    fun putInt(sp: SharedPreferences, key: String, value: Int) {
        val editor = getOrCreateEditor(sp)
        editor.putInt(key, value)
        saveChanges(editor)
    }

    fun putLong(sp: SharedPreferences,
                key: String,
                value: Long) {
        val editor = getOrCreateEditor(sp)
        editor.putLong(key, value)
        saveChanges(editor)
    }

    fun putBoolean(sp: SharedPreferences,
                   key: String,
                   value: Boolean) {
        val editor = getOrCreateEditor(sp)
        editor.putBoolean(key, value)
        saveChanges(editor)
    }

    fun removeKey(sp: SharedPreferences, key: String) {
        val editor = getOrCreateEditor(sp)
        editor.remove(key)
        saveChanges(editor)
    }

    fun clear(sp: SharedPreferences) {
        val editor = getOrCreateEditor(sp)
        editor.clear()
        saveChanges(editor)
    }

    private fun getSharedPreferences(context: Context,
                                     name: String,
                                     mode: Int = Context.MODE_PRIVATE): SharedPreferences =
            if (name.isEmpty()) {
                getDefaultSharedPreferences(context)
            } else {
                context.getSharedPreferences(name, mode)
            }

    @SuppressLint("CommitPrefEdits")
    private fun getOrCreateEditor(sp: SharedPreferences): SharedPreferences.Editor {
        var editor = sharedPreferencesEditorMap[sp]
        if (editor == null) {
            editor = sp.edit()
            sharedPreferencesEditorMap[sp] = editor
        }
        return editor as SharedPreferences.Editor
    }

    private fun saveChanges(editor: SharedPreferences.Editor, async: Boolean = true) {
        if (async) {
            editor.apply()
        } else {
            editor.commit()
        }
    }

    private fun getDefaultSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}
