package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.create.fragment;


import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEvent;

public class OnCreateFragmentEvent implements ScreenEvent {
    private Fragment fragment;

    public OnCreateFragmentEvent(Fragment fragment) {
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
