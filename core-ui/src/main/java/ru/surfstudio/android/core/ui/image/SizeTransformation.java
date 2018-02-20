package ru.surfstudio.android.core.ui.image;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import static ru.surfstudio.android.core.ui.image.ImageLoader.NO_SIZE;


/**
 * Трансформатор, ужимающий изображение по максимальной высоте или ширине без нарушение аспекта
 */
public class SizeTransformation extends BitmapTransformation {

    private boolean filterOnScale;
    private int maxWidth;
    private int maxHeight;

    public SizeTransformation(Context context, int maxWidth, int maxHeight, boolean filterOnScale) {
        super(context);
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.filterOnScale = filterOnScale;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        if (maxHeight == -1 && maxWidth == -1) {
            return toTransform;
        }
        int originalWidth = toTransform.getWidth();
        int originalHeight = toTransform.getHeight();
        float widthFactor = maxWidth == NO_SIZE ? Float.MIN_VALUE : 1.0f * originalWidth / maxWidth;
        float heightFactor = maxHeight == NO_SIZE ? Float.MIN_VALUE : 1.0f * originalHeight / maxHeight;
        float scaleFactor = Math.max(heightFactor, widthFactor);
        int newHeight = (int) (originalHeight / scaleFactor);
        int newWidth = (int) (originalWidth / scaleFactor);
        Bitmap bitmap = Bitmap.createScaledBitmap(toTransform, newWidth, newHeight, filterOnScale);
        toTransform.recycle();
        return bitmap;
    }

    @Override
    public String getId() {
        return "SizeTransformation(maxWidth=" + maxWidth + ", maxHeight=" + maxHeight
                + ", filterOnScale=" + filterOnScale + ")";
    }
}
