package ru.surfstudio.android.util


import android.annotation.SuppressLint
import android.text.TextPaint
import android.text.style.URLSpan

@SuppressLint("ParcelCreator")
class URLSpanNoUnderline(url: String) : URLSpan(url) {

    override fun updateDrawState(textPaint: TextPaint) {
        super.updateDrawState(textPaint)
        textPaint.isUnderlineText = false
    }
}
