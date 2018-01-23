package ru.surfstudio.android.core.ui.base.screen.configurator;

import android.support.v4.app.Fragment;

//todo задел на будующее для фрагмента контейнера
public class BaseFragmentConfigurator implements Configurator {

    private Fragment target;

    public BaseFragmentConfigurator(Fragment target) {
        this.target = target;
    }

    @Override
    public void run() {
        //empty
    }

    @Override
    public String getName() {
        return target.getClass().getCanonicalName();
    }
}
