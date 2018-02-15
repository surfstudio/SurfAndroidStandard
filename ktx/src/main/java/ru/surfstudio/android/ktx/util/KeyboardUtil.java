package ru.surfstudio.android.ktx.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Утилита для работы с экранной клавиатурой
 */
public class KeyboardUtil {

    /**
     * Показать экранную клавиатуру. <p>
     * Клавиатура открывается с нужным для указанного {@link EditText} {@link android.text.InputType}.
     *
     * @param editText {@link EditText}, для которого открывается клавиатура
     */

    public static void showKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.postDelayed(() -> imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT), 50);
    }

    /**
     * Скрыть экранную клавиатуру
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Скрыть экранную клавиатуру с вьюшки
     */
    public static void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private KeyboardUtil() {
        throw new IllegalStateException(KeyboardUtil.class + " не может иметь инстансов");
    }
}