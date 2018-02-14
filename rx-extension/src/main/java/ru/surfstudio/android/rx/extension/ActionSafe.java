package ru.surfstudio.android.rx.extention;

import io.reactivex.functions.Action;


/**
 * {@link Action} без обязательной
 * проверки на {@link Exception}
 *
 * @see Action
 */
public interface ActionSafe extends Action {
    void run();
}