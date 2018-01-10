package ru.surfstudio.standard.ui.util;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;

/**
 * Общие утилиты для работы с View
 */
public final class ViewUtils {
    private ViewUtils() {
        throw new IllegalStateException(ViewUtils.class.getName() +
                " не может иметь инстанс");
    }

    /**
     * Конвертация dp в пиксели
     */
    public static int convertDpToPx(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getDisplayMetrics(context));
    }

    /**
     * Конвертация пискелей в dp
     */
    public static float convertPxToDp(Context context, int px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, getDisplayMetrics(context));
    }

    public static ViewTreeObserver.OnGlobalLayoutListener keyboardVisibilityToggleListener(Activity activity,
                                                                                           KeyboardVisibilityToggleListener listener) {
        return () -> {
            View rootView = activity.getWindow().getDecorView();

            Rect rect = new Rect();
            rootView.getWindowVisibleDisplayFrame(rect);

            int screenHeight = rootView.getHeight();
            int keypadHeight = screenHeight - rect.bottom;

            listener.onKeyboardVisibilityToggle(keypadHeight > screenHeight * 0.15);
        };
    }

    static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static Bitmap getBitmapFromDrawable(Context context, @DrawableRes int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawableCompat || drawable instanceof VectorDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    /**
     * Возвращает ширину или высоту экрана
     */
    public static int getDisplayParam(Activity activity, DisplayParam param) {
        DisplayMetrics metrics = getDisplayMetrics(activity);
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        switch (param) {
            case WIDTH:
                return metrics.widthPixels;
            case HEIGHT:
                return metrics.heightPixels;
            default:
                throw new IllegalArgumentException("Unsupported DisplayParam: " + param.name());
        }
    }

    /**
     * Убирает подчеркивание нескольких EditText , если они дисэйблятся все вместе
     *
     * @param shouldDisabled - флаг отключения
     * @param eds            - varargs с EditText
     */
    public static void removeUnderlineFromEditTexts(boolean shouldDisabled, EditText... eds) {
        for (EditText ed : eds) {
            removeUnderlineFromEditText(shouldDisabled, ed);
        }
    }

    /**
     * Убирает подчеркивание с конкретного EditText
     *
     * @param shouldDisabled - флаг отключения
     * @param editText
     */
    private static void removeUnderlineFromEditText(boolean shouldDisabled, EditText editText) {
        if (shouldDisabled) {
            editText.setBackground(null);
        }
    }

    /**
     * Меняет цвет background у view
     */
    public static void changeViewBackgroundColor(View view, @ColorInt int color) {
        Drawable background = view.getBackground().mutate();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(color);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(color);
        }
    }

    public interface KeyboardVisibilityToggleListener {

        void onKeyboardVisibilityToggle(boolean visible);
    }
}