package ru.surfstudio.android.google_pay_button_sample

import android.content.Context
import android.util.AttributeSet
import ru.surfstudio.android.google_pay_button.GooglePayButton

/** Custom project implementation of black [GooglePayButton] with round corners. */
internal class RoundBlackGooglePayButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : GooglePayButton(context, attrs, defStyleAttr) {

    override var backgroundOverrideDrawableResources: Map<Style, Int> = hashMapOf(
            Style.BLACK_LOGO to R.drawable.bg_google_pay_button_black_round,
            Style.BLACK_TEXT to R.drawable.bg_google_pay_button_black_round
    )

    init {
        readAttrs(attrs)
    }
}