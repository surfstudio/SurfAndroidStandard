package ru.surfstudio.android.core.ui.message;


import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.view.View;

/**
 * Базовый класс контроллера отображения сообщений
 * Максимальное количество линий задается в integers:design_snackbar_text_max_lines
 */
public interface MessageController {
    void show(@StringRes int stringId);

    void show(String message);

    void show(String message, int backgroundColor);

    void show(@StringRes int stringId,
              @StringRes int actionStringId,
              @ColorRes int buttonColor,
              View.OnClickListener listener);

    void show(@StringRes int stringId,
              @ColorRes int backgroundColor,
              @StringRes int actionStringId,
              @ColorRes int buttonColor);

    void showToast(@StringRes int stringId);

    void showToast(@StringRes int stringId, int gravity);

    void showToast(String message);

    void showToast(String message, int gravity);
}
