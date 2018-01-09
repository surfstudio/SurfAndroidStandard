package ru.surfstudio.android.core.ui.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.widget.ImageView;


/**
 * Утилита для работы с Drawable
 */
public class DrawableUtil {

    private DrawableUtil() {
        //do nothing
    }

    /**
     * Конвертация Bitmap в Drawable
     */
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * утилита для запуска анимации у animated-vector
     * */
    public static void makeAvdAnimation(Context context, final ImageView imageView, @DrawableRes final int drawableId) {
        AnimatedVectorDrawableCompat avd = AnimatedVectorDrawableCompat.create(context, drawableId);
        imageView.setImageDrawable(avd);

        Drawable drawable = imageView.getDrawable();

        if (drawable instanceof Animatable) {
            ((Animatable)drawable).start();
        } else {
            throw new IllegalArgumentException("Drawable must be Animatable");
        }
    }

    public static void startAvdAnimation(final ImageView imageView) {
        if (!(imageView.getDrawable() instanceof AnimatedVectorDrawableCompat)) {
            return;
        }
        AnimatedVectorDrawableCompat animation = (AnimatedVectorDrawableCompat) imageView.getDrawable();
        animation.start();
    }
}