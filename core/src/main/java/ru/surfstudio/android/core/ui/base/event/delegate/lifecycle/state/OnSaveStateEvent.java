package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.state;


import android.os.Bundle;

import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEvent;

public class OnSaveStateEvent implements ScreenEvent {

    private final Bundle outState;

    public OnSaveStateEvent(Bundle outState){
        this.outState = outState;
    }

    public Bundle getOutState() {
        return outState;
    }

}
