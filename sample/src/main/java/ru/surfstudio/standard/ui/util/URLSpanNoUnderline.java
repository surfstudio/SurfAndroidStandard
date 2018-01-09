package ru.surfstudio.standard.ui.util;


import android.annotation.SuppressLint;
import android.text.TextPaint;
import android.text.style.URLSpan;

@SuppressLint("ParcelCreator")
public class URLSpanNoUnderline extends URLSpan {
    public URLSpanNoUnderline(String url) {
        super(url);
    }

    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setUnderlineText(false);
    }
}
