package ru.surfstudio.android.google_pay_button

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
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
 * ### **`gpcTextHeightPercent`**:
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
class GooglePayButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val gpayButton: View
    private val gpayButtonText: View

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.GooglePayButton)
        val buttonStyle = ta.getInteger(
                R.styleable.GooglePayButton_gpbStyle,
                Style.BLACK_GPAY.ordinal
        ).let(Style.Companion::getByOrdinal)
        val textHeightPercent = ta.getFloat(
                R.styleable.GooglePayButton_gpcTextHeightPercent,
                DEFAULT_HEIGHT_PERCENTAGE
        )
        ta.recycle()

        val layoutResId = when (buttonStyle) {
            Style.BLACK_GPAY -> R.layout.google_pay_button_black
            Style.BLACK_BUY_WITH_GPAY -> R.layout.google_pay_button_black_buy_with_gpay
            Style.BLACK_FLAT_GPAY -> R.layout.google_pay_button_black_flat
            Style.BLACK_FLAT_BUY_WITH_GPAY -> R.layout.google_pay_button_black_flat_buy_with_gpay
            Style.WHITE_GPAY -> R.layout.google_pay_button_white
            Style.WHITE_BUTTON_WITH_GPAY -> R.layout.google_pay_button_white_buy_with_gpay
            Style.WHITE_FLAT_GPAY -> R.layout.google_pay_button_white_flat
            Style.WHITE_FLAT_BUY_WITH_GPAY -> R.layout.google_pay_button_white_flat_buy_with_gpay
        }
        inflate(context, layoutResId, this)
        gpayButton = findViewById(R.id.google_pay_button)
        gpayButtonText = findViewById(R.id.google_pay_button_text)
        gpayButtonText.updateLayoutParams<ConstraintLayout.LayoutParams> {
            matchConstraintPercentHeight = textHeightPercent
        }
    }

    override fun isEnabled(): Boolean {
        return gpayButton.isEnabled
    }

    override fun isClickable(): Boolean {
        return gpayButton.isClickable
    }

    override fun isFocused(): Boolean {
        return gpayButton.isFocused
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        gpayButton.setOnClickListener(listener)
    }

    override fun setEnabled(isEnabled: Boolean) {
        gpayButton.isEnabled = isEnabled
    }

    override fun setClickable(isClickable: Boolean) {
        gpayButton.isClickable = isClickable
    }

    override fun setFocusable(isFocusable: Boolean) {
        gpayButton.isFocusable = isFocusable
    }

    /** Visual style of [GooglePayButton]. */
    enum class Style {
        BLACK_GPAY,
        BLACK_BUY_WITH_GPAY,
        BLACK_FLAT_GPAY,
        BLACK_FLAT_BUY_WITH_GPAY,
        WHITE_GPAY,
        WHITE_BUTTON_WITH_GPAY,
        WHITE_FLAT_GPAY,
        WHITE_FLAT_BUY_WITH_GPAY;

        companion object {

            /** Get [Style] by it's ordinal. Fallback is [BLACK_GPAY]. */
            fun getByOrdinal(ordinal: Int): Style {
                return values().getOrNull(ordinal) ?: BLACK_GPAY
            }
        }
    }

    private companion object {
        const val DEFAULT_HEIGHT_PERCENTAGE = 0.5f
    }
}
