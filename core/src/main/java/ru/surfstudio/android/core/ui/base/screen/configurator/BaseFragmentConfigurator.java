package ru.surfstudio.android.core.ui.base.screen.configurator;

import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;

//todo для фрагмента контейнера
public class BaseFragmentConfigurator implements Configurator {

    private Fragment target;
    private PersistentScope persistentScreenScope;

    public BaseFragmentConfigurator(Fragment target) {
        this.target = target;
    }

    @Override
    public void run() {
        //empty
    }

    @Override
    public void setPersistentScope(PersistentScope persistentScreenScope) {
        this.persistentScreenScope = persistentScreenScope;
    }

    protected PersistentScope getPersistentScope() {
        return persistentScreenScope;
    }

    @Override
    public String getName() {
        return target.getClass().getCanonicalName();
    }
}
