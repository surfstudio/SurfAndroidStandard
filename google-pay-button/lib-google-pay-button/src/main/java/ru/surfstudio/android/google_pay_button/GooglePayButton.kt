/*
  Copyright (c) 2020-present, SurfStudio LLC. Dmitry Kuzmenchuk.

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
package ru.surfstudio.android.google_pay_button

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.updateLayoutParams

/**
 * Reusable and styleable Google Pay button.
 *
 * To use and customize this button you should implement your own project implementation of this button,
 * setup some [backgroundOverrideDrawableResources] and [contentOverrideDrawableResources] if necessary,
 * and at the end call [initializeInternal] method in the constructor.
 *
 * You can change [GooglePayButton.Style] in the runtime with [setStyle] method which will recreate
 * and reconfigure views to fit new style well.
 *
 * Also you can change content scale by [setContentScale] at the runtime as well.
 * */
abstract class GooglePayButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /** Map of "style to drawable resource" which allow developer to override background of the concrete style. */
    protected open var backgroundOverrideDrawableResources: Map<Style, Int> = emptyMap()

    /** Map of "style to drawable resource" which allow developer to override content of the concrete style. */
    protected open var contentOverrideDrawableResources: Map<Style, Int> = emptyMap()

    /** Current applied style of this button. */
    protected var currentStyle: Style? = null
        private set

    /** Scale of the content drawable of this button. */
    protected var contentScale: Float = DEFAULT_CONTENT_SCALE
        private set

    private var isEnabledInternal: Boolean = true
    private var isClickableInternal: Boolean = true
    private var isFocusedInternal: Boolean = false
    private var isFocusableInternal: Boolean = true
    private var onClickListenerInternal: OnClickListener? = null

    private var googlePayButtonView: ConstraintLayout? = null
    private var googlePayButtonContentView: ImageView? = null

    override fun isEnabled(): Boolean {
        return isEnabledInternal
    }

    override fun isClickable(): Boolean {
        return isClickableInternal
    }

    override fun isFocused(): Boolean {
        return isFocusedInternal
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        onClickListenerInternal = listener
        setOnClickListenerInternal()
    }

    override fun setEnabled(isEnabled: Boolean) {
        isEnabledInternal = isEnabled
        setIsEnabledInternal()
    }

    override fun setClickable(isClickable: Boolean) {
        isClickableInternal = isClickable
        setClickableInternal()
    }

    override fun setFocusable(isFocusable: Boolean) {
        isFocusableInternal = isFocusable
        setFocusableInternal()
    }

    /** Set [Style] of this button. Recreate if necessary and apply previous view configuration. */
    fun setStyle(style: Style) {
        if (style != currentStyle) {
            inflateWithStyle(style)
        }
        setOnClickListenerInternal()
        setIsEnabledInternal()
        setClickableInternal()
        setFocusableInternal()
        setContentScaleInternal()
    }

    /** Set scale of the content inside button (logotype or text).  */
    fun setContentScale(scale: Float = DEFAULT_CONTENT_SCALE) {
        contentScale = try {
            scale / 2f
        } catch (error: ArithmeticException) {
            DEFAULT_CONTENT_SCALE
        }
        setContentScaleInternal()
    }

    /**
     * Read attributes, (re-)create views and apply previous available view configuration.
     *
     * **Should be called from constructor of child.**
     * */
    protected fun initializeInternal(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.GooglePayButton) {
            val contentScale = getFloat(R.styleable.GooglePayButton_gpbContentScale, DEFAULT_CONTENT_SCALE)
            val style = getInteger(R.styleable.GooglePayButton_gpbStyle, Style.BLACK_LOGO.ordinal)
                    .let { Style.getByOrdinal(it) }
            setContentScale(contentScale)
            setStyle(style)
        }
    }

    private fun inflateWithStyle(style: Style) {
        googlePayButtonContentView = null
        googlePayButtonView = null
        removeAllViews()
        val layoutResId = when (style) {
            Style.BLACK_LOGO -> R.layout.google_pay_button_black_logo
            Style.BLACK_TEXT -> R.layout.google_pay_button_black_text
            Style.BLACK_FLAT_LOGO -> R.layout.google_pay_button_black_flat_logo
            Style.BLACK_FLAT_TEXT -> R.layout.google_pay_button_black_flat_text
            Style.WHITE_LOGO -> R.layout.google_pay_button_white_logo
            Style.WHITE_TEXT -> R.layout.google_pay_button_white_text
            Style.WHITE_FLAT_LOGO -> R.layout.google_pay_button_white_flat_logo
            Style.WHITE_FLAT_TEXT -> R.layout.google_pay_button_white_flat_text
        }
        inflate(context, layoutResId, this)
        googlePayButtonView = findViewById(R.id.google_pay_button_view)
        googlePayButtonContentView = findViewById(R.id.google_pay_button_content_view)
        backgroundOverrideDrawableResources[style]?.let { backgroundOverride ->
            googlePayButtonView?.setBackgroundResource(backgroundOverride)
        }
        contentOverrideDrawableResources[style]?.let { contentOverride ->
            googlePayButtonContentView?.setImageResource(contentOverride)
        }
    }

    private fun setOnClickListenerInternal() {
        googlePayButtonView?.setOnClickListener(onClickListenerInternal)
    }

    private fun setIsEnabledInternal() {
        googlePayButtonView?.isEnabled = isEnabledInternal
    }

    private fun setClickableInternal() {
        googlePayButtonView?.isClickable = isClickableInternal
    }

    private fun setFocusableInternal() {
        googlePayButtonView?.isFocusable = isFocusableInternal
    }

    private fun setContentScaleInternal() {
        googlePayButtonContentView?.updateLayoutParams<ConstraintLayout.LayoutParams> {
            matchConstraintPercentHeight = contentScale
        }
    }

    /** Visual style of [GooglePayButton]. */
    enum class Style {

        /**
         * Black button with "GPay" logotype,
         * corresponds to the [R.layout.google_pay_button_black_logo] layout.
         * */
        BLACK_LOGO,

        /**
         * Black button with "Buy with GPay" text,
         * corresponds to the [R.layout.google_pay_button_black_text] layout.
         * */
        BLACK_TEXT,

        /**
         * Black flat button with "GPay" logotype,
         * corresponds to the [R.layout.google_pay_button_black_flat_logo] layout.
         * */
        BLACK_FLAT_LOGO,

        /**
         * Black flat button with "Buy with GPay" text,
         * corresponds to the [R.layout.google_pay_button_black_flat_text] layout.
         * */
        BLACK_FLAT_TEXT,

        /**
         * White button with "GPay" logotype,
         * corresponds to the [R.layout.google_pay_button_white_logo] layout.
         * */
        WHITE_LOGO,

        /**
         * White button with "Buy with GPay" text,
         * corresponds to the [R.layout.google_pay_button_white_text] layout.
         * */
        WHITE_TEXT,

        /**
         * White flat button with "GPay" logotype,
         * corresponds to the [R.layout.google_pay_button_white_flat_logo] layout.
         * */
        WHITE_FLAT_LOGO,

        /**
         * White flat button with "Buy with GPay" text,
         * corresponds to the [R.layout.google_pay_button_white_flat_text] layout.
         * */
        WHITE_FLAT_TEXT;

        companion object {

            /** Get [Style] by it's ordinal, if not found -- [fallback] returned. */
            fun getByOrdinal(ordinal: Int, fallback: Style = BLACK_LOGO): Style {
                return values().getOrNull(ordinal) ?: fallback
            }
        }
    }

    companion object {

        /** Default scale of the content (text or logotype) inside button. */
        const val DEFAULT_CONTENT_SCALE = 1f
    }
}
