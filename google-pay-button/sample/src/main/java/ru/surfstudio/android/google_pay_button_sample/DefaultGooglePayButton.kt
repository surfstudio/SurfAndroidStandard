package ru.surfstudio.android.google_pay_button_sample

import android.content.Context
import android.util.AttributeSet
import ru.surfstudio.android.google_pay_button.GooglePayButton

/** Default project implementation of [GooglePayButton] without any changes. */
internal class DefaultGooglePayButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : GooglePayButton(context,  attrs, defStyleAttr){

    init {
        initializeInternal(attrs)
    }
}