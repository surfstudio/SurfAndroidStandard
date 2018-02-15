package ru.surfstudio.android.core.ui.base.screen.event.lifecycle.state;


import android.os.Bundle;

import ru.surfstudio.android.core.ui.base.screen.event.base.ScreenEvent;

/**
 * событие сохранения состояния
 */
public class OnSaveStateEvent implements ScreenEvent {

    private final Bundle outState;

    public OnSaveStateEvent(Bundle outState){
        this.outState = outState;
    }

    public Bundle getOutState() {
        return outState;
    }

}
