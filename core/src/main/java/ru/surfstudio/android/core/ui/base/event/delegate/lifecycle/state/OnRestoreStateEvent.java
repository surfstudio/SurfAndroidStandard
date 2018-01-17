package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.state;


import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEvent;

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
