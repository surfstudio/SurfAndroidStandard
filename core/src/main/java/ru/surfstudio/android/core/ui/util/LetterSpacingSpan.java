package ru.surfstudio.android.core.ui.util;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.style.ReplacementSpan;


/**
 * Спан, устанавливающий межсимвольные отступы.
 */
public class LetterSpacingSpan extends ReplacementSpan {
    private final int spacePx;

    /**
     * конструктор
     *
     * @param spacePx отсутп в пикселях
     */
    public LetterSpacingSpan(int spacePx) {
        this.spacePx = spacePx;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text,
                       int start, int end, Paint.FontMetricsInt fm) {
        return (int) (paint.measureText(text, start, end)
                + spacePx * (end - start - 1));
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text,
                     int start, int end, float x, int top, int y,
                     int bottom, @NonNull Paint paint) {
        float dx = x;
        for (int i = start; i < end; i++) {
            canvas.drawText(text, i, i + 1, dx, y, paint);
            dx += paint.measureText(text, i, i + 1) + spacePx;
        }
    }
}