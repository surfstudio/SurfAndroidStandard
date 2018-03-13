package ru.surfstudio.android.core.ui.event.lifecycle.state;


import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.event.base.ScreenEvent;

/**
 * событие восстановления состояния
 */
public class OnRestoreStateEvent implements ScreenEvent {
    @Nullable
    private Bundle savedInstanceState;

    public OnRestoreStateEvent(@Nullable Bundle outState) {
        this.savedInstanceState = outState;
    }

    @Nullable
    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }

}
