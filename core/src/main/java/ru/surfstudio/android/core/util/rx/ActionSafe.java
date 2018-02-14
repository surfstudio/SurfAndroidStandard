package ru.surfstudio.android.core.util.rx;

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