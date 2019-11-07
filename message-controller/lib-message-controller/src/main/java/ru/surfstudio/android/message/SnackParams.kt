package ru.surfstudio.android.message

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

/**
 * Container for Snack parameters;
 * priority: resource id
 */
data class SnackParams(
        @StringRes
        val messageResId: Int = 0,
        val message: String = "",
        @ColorRes
        val backgroundColorResId: Int = 0,
        @ColorInt
        val backgroundColor: Int? = null,
        @StringRes
        val actionResId: Int = 0,
        val action: String = "",
        @ColorRes
        val actionColorResId: Int = 0,
        val actionColor: Int? = null,
        val duration: Int = Snackbar.LENGTH_SHORT
)