package ru.surfstudio.android.core.ui.base.dagger.provider;


import ru.surfstudio.android.core.ui.base.scope.PersistentScope;


public abstract class Provider<T> {
    final PersistentScope screenScope;

    Provider(PersistentScope screenScope) {
        this.screenScope = screenScope;
    }

    public abstract T get();
}
