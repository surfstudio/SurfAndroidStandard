package ru.surfstudio.android.core.ui.base.event.delegate.activity.result;


import android.content.Intent;

import java.io.Serializable;

/**
 * интерфейс для Route, работающего через onActivityResult
 * @param <T>
 */
public interface SupportOnActivityResultRoute<T extends Serializable> {
    String EXTRA_RESULT = "extra_result";

    Intent prepareResultIntent(T resultData);

    T parseResultIntent(Intent resultIntent);

    int getRequestCode();
}
