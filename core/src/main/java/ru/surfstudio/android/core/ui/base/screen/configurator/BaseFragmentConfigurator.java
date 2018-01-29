package ru.surfstudio.android.core.ui.base.screen.configurator;

import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.base.screen.scope.FragmentPersistentScope;

//todo для фрагмента контейнера
public class BaseFragmentConfigurator implements Configurator<FragmentPersistentScope> {

    private Fragment target;
    private FragmentPersistentScope persistentScreenScope;

    public BaseFragmentConfigurator(Fragment target) {
        this.target = target;
    }

    @Override
    public void run() {
        //empty
    }

    @Override
    public void setPersistentScope(FragmentPersistentScope persistentScreenScope) {
        this.persistentScreenScope = persistentScreenScope;
    }

    protected FragmentPersistentScope getPersistentScope() {
        return persistentScreenScope;
    }

    @Override
    public String getName() {
        return target.getClass().getCanonicalName();
    }
}
