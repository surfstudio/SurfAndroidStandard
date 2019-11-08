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

/**
 * хелпер для работы с SharedPref
 */
const val NO_BACKUP_SHARED_PREF = "NO_BACKUP_SHARED_PREF"
const val BACKUP_SHARED_PREF = "BACKUP_SHARED_PREF"

object SettingsUtil {

    const val EMPTY_BOOLEAN_SETTING = false
    const val EMPTY_INT_SETTING = -1
    const val EMPTY_LONG_SETTING = -1L
    const val EMPTY_FLOAT_SETTING = -1f
    const val EMPTY_DOUBLE_SETTING = -1.0
    val EMPTY_SET_SETTING = HashSet<String>()
    val EMPTY_STRING_SETTING = EMPTY_STRING

    private val sharedPreferencesEditorMap = mutableMapOf<SharedPreferences, SharedPreferences.Editor>()

    fun getBoolean(
            context: Context,
            key: String,
            defaultValue: Boolean = EMPTY_BOOLEAN_SETTING,
            prefName: String = EMPTY_STRING
    ) = getBoolean(getSharedPreferences(context, prefName), key, defaultValue)

    fun getString(
            context: Context,
            key: String,
            defaultValue: String = EMPTY_STRING_SETTING,
            prefName: String = EMPTY_STRING
    ) = getString(getSharedPreferences(context, prefName), key, defaultValue)

    fun getStringSet(
            context: Context,
            key: String,
            defaultValue: Set<String> = EMPTY_SET_SETTING,
            prefName: String = EMPTY_STRING
    ) = getStringSet(getSharedPreferences(context, prefName), key, defaultValue)

    fun getInt(
            context: Context,
            key: String,
            defaultValue: Int = EMPTY_INT_SETTING,
            prefName: String = EMPTY_STRING
    ) = getInt(getSharedPreferences(context, prefName, defaultValue), key)

    fun getLong(
            context: Context,
            key: String,
            defaultValue: Long = EMPTY_LONG_SETTING,
            prefName: String = EMPTY_STRING
    ) = getLong(getSharedPreferences(context, prefName), key, defaultValue)


    fun getFloat(
            context: Context,
            key: String,
            defaultValue: Float = EMPTY_FLOAT_SETTING,
            prefName: String = EMPTY_STRING
    ) = getFloat(getSharedPreferences(context, prefName), key, defaultValue)

    fun getDouble(
            context: Context,
            key: String,
            defaultValue: Double = EMPTY_DOUBLE_SETTING,
            prefName: String = EMPTY_STRING
    ) = getDouble(getSharedPreferences(context, prefName), key, defaultValue)

    fun putBoolean(
            context: Context,
            key: String,
            value: Boolean,
            prefName: String = EMPTY_STRING,
            async: Boolean = true
    ) {
        putBoolean(getSharedPreferences(context, prefName), key, value, async)
    }

    fun putString(
            context: Context,
            key: String,
            value: String,
            prefName: String = EMPTY_STRING,
            async: Boolean = true
    ) {
        putString(getSharedPreferences(context, prefName), key, value, async)
    }

    fun putInt(
            context: Context,
            key: String,
            value: Int,
            prefName: String = EMPTY_STRING,
            async: Boolean = true
    ) {
        putInt(getSharedPreferences(context, prefName), key, value, async)
    }

    fun putLong(
            context: Context,
            key: String,
            value: Long,
            prefName: String = EMPTY_STRING,
            async: Boolean = true
    ) {
        putLong(getSharedPreferences(context, prefName), key, value, async)
    }

    fun putFloat(
            context: Context,
            key: String,
            value: Float,
            prefName: String = EMPTY_STRING,
            async: Boolean = true
    ) {
        putFloat(getSharedPreferences(context, prefName), key, value, async)
    }

    fun putDouble(
            context: Context,
            key: String,
            value: Double,
            prefName: String = EMPTY_STRING,
            async: Boolean = true
    ) {
        putDouble(getSharedPreferences(context, prefName), key, value, async)
    }

    fun removeKey(
            context: Context,
            key: String,
            prefName: String = EMPTY_STRING,
            async: Boolean = true
    ) {
        removeKey(getSharedPreferences(context, prefName), key, async)
    }

    fun clear(
            context: Context,
            prefName: String = EMPTY_STRING,
            async: Boolean = true
    ) {
        clear(getSharedPreferences(context, prefName), async)
    }

    fun getBoolean(
            sp: SharedPreferences,
            key: String,
            defaultValue: Boolean = EMPTY_BOOLEAN_SETTING
    ) = sp.getBoolean(key, defaultValue)

    fun getString(
            sp: SharedPreferences,
            key: String,
            defaultValue: String = EMPTY_STRING_SETTING
    ) = sp.getString(key, defaultValue) ?: EMPTY_STRING

    fun getStringSet(
            sp: SharedPreferences,
            key: String,
            defaultValue: Set<String> = EMPTY_SET_SETTING
    ) = sp.getStringSet(key, defaultValue) ?: setOf()

    fun getInt(
            sp: SharedPreferences,
            key: String,
            defaultValue: Int = EMPTY_INT_SETTING
    ) = sp.getInt(key, defaultValue)

    fun getLong(
            sp: SharedPreferences,
            key: String,
            defaultValue: Long = EMPTY_LONG_SETTING
    ) = sp.getLong(key, defaultValue)

    fun getFloat(
            sp: SharedPreferences,
            key: String,
            defaultValue: Float = EMPTY_FLOAT_SETTING
    ) = sp.getFloat(key, defaultValue)

    fun getDouble(
            sp: SharedPreferences,
            key: String,
            defaultValue: Double = EMPTY_DOUBLE_SETTING
    ) = Double.fromBits(sp.getLong(key, defaultValue.toBits()))

    fun putBoolean(
            sp: SharedPreferences,
            key: String,
            value: Boolean,
            async: Boolean = true
    ) {
        val editor = getOrCreateEditor(sp)
        editor.putBoolean(key, value)
        saveChanges(editor, async)
    }

    fun putString(
            sp: SharedPreferences,
            key: String,
            value: String,
            async: Boolean = true
    ) {
        val editor = getOrCreateEditor(sp)
        editor.putString(key, value)
        saveChanges(editor, async)
    }

    fun putStringSet(
            sp: SharedPreferences,
            key: String,
            value: Set<String>,
            async: Boolean = true
    ) {
        val editor = getOrCreateEditor(sp)
        editor.putStringSet(key, value)
        saveChanges(editor, async)
    }

    fun putInt(
            sp: SharedPreferences,
            key: String,
            value: Int,
            async: Boolean = true
    ) {
        val editor = getOrCreateEditor(sp)
        editor.putInt(key, value)
        saveChanges(editor, async)
    }

    fun putLong(
            sp: SharedPreferences,
            key: String,
            value: Long,
            async: Boolean = true
    ) {
        val editor = getOrCreateEditor(sp)
        editor.putLong(key, value)
        saveChanges(editor, async)
    }

    fun putFloat(
            sp: SharedPreferences,
            key: String,
            value: Float,
            async: Boolean = true
    ) {
        val editor = getOrCreateEditor(sp)
        editor.putFloat(key, value)
        saveChanges(editor, async)
    }

    fun putDouble(
            sp: SharedPreferences,
            key: String,
            value: Double,
            async: Boolean = true
    ) {
        val editor = getOrCreateEditor(sp)
        editor.putLong(key, value.toBits())
        saveChanges(editor, async)
    }

    fun removeKey(sp: SharedPreferences, key: String, async: Boolean = true) {
        val editor = getOrCreateEditor(sp)
        editor.remove(key)
        saveChanges(editor, async)
    }

    fun clear(sp: SharedPreferences, async: Boolean = true) {
        val editor = getOrCreateEditor(sp)
        editor.clear()
        saveChanges(editor, async)
    }

    private fun getSharedPreferences(
            context: Context,
            name: String,
            mode: Int = Context.MODE_PRIVATE
    ): SharedPreferences =
            if (name.isEmpty()) {
                getDefaultSharedPreferences(context)
            } else {
                context.getSharedPreferences(name, mode)
            }

    @SuppressLint("CommitPrefEdits")
    private fun getOrCreateEditor(sp: SharedPreferences): SharedPreferences.Editor =
            sharedPreferencesEditorMap.getOrPut(sp) {
                sp.edit()
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
