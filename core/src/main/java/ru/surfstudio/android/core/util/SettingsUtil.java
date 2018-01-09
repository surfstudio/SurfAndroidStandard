package ru.surfstudio.android.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

/**
 * хелпер для работы с SharedPref
 */
public class SettingsUtil {
    public static final String EMPTY_STRING_SETTING = "";
    public static final int EMPTY_INT_SETTING = -1;
    public static final long EMPTY_LONG_SETTING = -1L;

    private SettingsUtil() {
        //do nothing
    }

    public static String getString(Context context, String key) {
        return getString(getDefaultSharedPreferences(context), key);
    }

    public static void putString(Context context, String key, String value) {
        putString(getDefaultSharedPreferences(context), key, value);
    }

    public static void putInt(Context context, String key, int value) {
        putInt(getDefaultSharedPreferences(context), key, value);
    }

    public static void putLong(Context context, String key, long value) {
        putLong(getDefaultSharedPreferences(context), key, value);
    }

    public static int getInt(Context context, String key) {
        return getInt(getDefaultSharedPreferences(context), key);
    }

    public static long getLong(Context context, String key) {
        return getLong(getDefaultSharedPreferences(context), key);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        putBoolean(getDefaultSharedPreferences(context), key, value);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getBoolean(getDefaultSharedPreferences(context), key, defaultValue);
    }

    public static String getString(SharedPreferences sp, String key) {
        return sp.getString(key, EMPTY_STRING_SETTING);
    }

    public static Set<String> getStringSet(SharedPreferences sp, String key) {
        return sp.getStringSet(key, new HashSet<String>());
    }

    public static void putStringSet(SharedPreferences sp, String key, Set<String> value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(key, value);
        saveChanges(editor);
    }

    public static void putString(SharedPreferences sp, String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        saveChanges(editor);
    }

    public static void putInt(SharedPreferences sp, String key, int value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        saveChanges(editor);
    }

    public static void putLong(SharedPreferences sp, String key, long value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        saveChanges(editor);
    }

    public static int getInt(SharedPreferences sp, String key) {
        return sp.getInt(key, EMPTY_INT_SETTING);
    }

    public static long getLong(SharedPreferences sp, String key) {
        return sp.getLong(key, EMPTY_LONG_SETTING);
    }

    public static void putBoolean(SharedPreferences sp, String key, boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        saveChanges(editor);
    }

    public static boolean getBoolean(SharedPreferences sp, String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    private static void saveChanges(SharedPreferences.Editor editor) {
        editor.apply();
    }

    private static SharedPreferences getDefaultSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
