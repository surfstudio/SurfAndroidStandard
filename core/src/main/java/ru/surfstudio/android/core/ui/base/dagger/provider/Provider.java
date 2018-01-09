package ru.surfstudio.android.core.ui.base.dagger.provider;


import com.agna.ferro.core.PersistentScreenScope;


public abstract class Provider<T> {
    final PersistentScreenScope screenScope;

    Provider(PersistentScreenScope screenScope) {
        this.screenScope = screenScope;
    }

    public abstract T get();
}
