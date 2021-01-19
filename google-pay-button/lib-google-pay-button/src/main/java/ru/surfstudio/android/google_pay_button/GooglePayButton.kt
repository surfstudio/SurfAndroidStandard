package ru.surfstudio.android.google_pay_button

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.updateLayoutParams

/**
 * Reusable and styleable Google Pay button.
 *
 * Should be inflated only through layout file.
 *
 * **For view customisation use attributes described below**.
 *
 * ####
 *
 * ### **`gpbTextHeightPercent`**:
 *
 * **Description**: Height of GPay text in percentage of height.
 *
 * **Possible values**: any value in range from `0.0` to `1.0`.
 *
 * **Default**: `0.5`.
 *
 * ####
 *
 * ### **`gpbStyle`**:
 *
 * **Description**: Visual style of this button created by official Google Guidelines.
 *
 * **Possible values**: `black_gpay`, `black_buy_with_gpay`, `black_flat_gpay`,
 * `black_flat_buy_with_gpay`, `white_gpay`, `white_buy_with_gpay`,
 * `white_flat_gpay`, `white_flat_buy_with_gpay`.
 *
 * **Default**: `black_gpay`.
 * */
@Deprecated("update doc")
class GooglePayButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    protected var currentStyle: Style? = null
        private set

    protected var contentScale: Float = DEFAULT_CONTENT_SCALE
        private set

    private var isEnabledInternal: Boolean = true
    private var isClickableInternal: Boolean = true
    private var isFocusedInternal: Boolean = false
    private var isFocusableInternal: Boolean = true
    private var onClickListenerInternal: OnClickListener? = null

    private var googlePayButtonView: View? = null
    private var googlePayButtonTextView: View? = null

    init {
        readAttrs(attrs)
    }

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

    protected fun readAttrs(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.GooglePayButton) {
            val contentScale = getFloat(R.styleable.GooglePayButton_gpbContentScale, DEFAULT_CONTENT_SCALE)
            val style = getInteger(R.styleable.GooglePayButton_gpbStyle, Style.BLACK_LOGO.ordinal)
                    .let { Style.getByOrdinal(it) }
            setContentScale(contentScale)
            setStyle(style)
        }
    }

    private fun inflateWithStyle(style: Style) {
        googlePayButtonTextView = null
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
        googlePayButtonTextView = findViewById(R.id.google_pay_button_text_view)
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
        googlePayButtonTextView?.updateLayoutParams<ConstraintLayout.LayoutParams> {
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
