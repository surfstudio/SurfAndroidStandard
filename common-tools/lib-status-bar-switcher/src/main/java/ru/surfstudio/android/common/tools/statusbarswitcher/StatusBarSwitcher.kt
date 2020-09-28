/*
  Copyright (c) 2020-present, SurfStudio LLC.

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
package ru.surfstudio.android.common.tools.statusbarswitcher

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.AsyncTask
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.ColorInt
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.palette.graphics.Palette
import kotlin.math.sqrt

/**
 * Helper class for switching status bar color automatically.
 *
 * Status bar color calculation based on the underlying content brightness.
 */
class StatusBarSwitcher(private val displayMetrics: DisplayMetrics) {

    private val colorFilter = Palette.Filter { _, _ -> true }

    private val width: Int
        get() = displayMetrics.widthPixels

    private var attachedActivity: Activity? = null

    private val decorView: View?
        get() = attachedActivity?.window?.decorView

    private var statusBarHeight: Int = 0

    private val statusBarHeightListener = OnApplyWindowInsetsListener { _, insets ->
        statusBarHeight = insets.stableInsetTop
        insets
    }

    private var hasStatusBarListenerAttached: Boolean = false

    private val onPreDrawListener = ViewTreeObserver.OnPreDrawListener {
        if (statusBarHeight != 0) {
            val bitmap = Bitmap.createBitmap(width, statusBarHeight, Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            val statusBarRect = Rect(0, 0, width, statusBarHeight)
            canvas.clipRect(statusBarRect)
            decorView?.draw(canvas)

            Palette.from(bitmap)
                    .clearFilters()
                    .addFilter(colorFilter)
                    .tryGenerate { palette ->
                        val dominantColor = palette.getDominantColor(Color.WHITE)
                        val isLightStatusBar = isBrightColor(dominantColor)
                        attachedActivity?.let { setLightStatusBar(it, isLightStatusBar) }
                    }
        }
        true
    }

    /**
     * Sets status bar height manually.
     */
    fun setStatusBarHeight(statusBarHeight: Int) {
        this.statusBarHeight = statusBarHeight
    }

    /**
     * Attaches [StatusBarSwitcher] to the [activity].
     *
     * @param shouldDetectStatusBarHeight defines whether status bar height should be calculated
     * automatically.
     */
    fun attach(
            activity: Activity,
            shouldDetectStatusBarHeight: Boolean = true
    ) {
        val decorView = activity.window.decorView
        decorView.viewTreeObserver.addOnPreDrawListener(onPreDrawListener)
        if (shouldDetectStatusBarHeight) {
            ViewCompat.setOnApplyWindowInsetsListener(decorView, statusBarHeightListener)
            hasStatusBarListenerAttached = true
        }
        attachedActivity = activity
    }

    /**
     * Detaches [StatusBarSwitcher] from the latest activity.
     */
    fun detach() {
        val activity = attachedActivity ?: return
        attachedActivity = null

        val decorView = activity.window.decorView ?: return
        if (hasStatusBarListenerAttached) {
            ViewCompat.setOnApplyWindowInsetsListener(decorView, null)
            hasStatusBarListenerAttached = false
        }
        decorView.viewTreeObserver.removeOnPreDrawListener(onPreDrawListener)
    }

    /**
     * Defines brightness of color.
     *
     * @return true, if color is bright; false otherwise.
     */
    private fun isBrightColor(@ColorInt color: Int): Boolean {
        val rgb = intArrayOf(Color.red(color), Color.green(color), Color.blue(color))
        val brightness = sqrt(
                rgb[0] * rgb[0] * .241 +
                        rgb[1] * rgb[1] * .691 +
                        rgb[2] * rgb[2] * .068
        )
        return brightness >= 200
    }

    private fun Palette.Builder.tryGenerate(onGeneratedAction: (Palette) -> Unit): AsyncTask<Bitmap, Void, Palette> {
        return this.generate { palette: Palette? ->
            palette ?: return@generate
            onGeneratedAction(palette)
        }
    }

    companion object {

        /**
         * Toggles status bar color for the defined [activity].
         */
        fun setLightStatusBar(activity: Activity, isLightStatusBar: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val window = activity.window
                val decorView = window?.decorView ?: return
                val systemUiVisibilityFlags = decorView.systemUiVisibility
                decorView.systemUiVisibility = when {
                    isLightStatusBar -> systemUiVisibilityFlags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    else -> systemUiVisibilityFlags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
            }
        }
    }
}