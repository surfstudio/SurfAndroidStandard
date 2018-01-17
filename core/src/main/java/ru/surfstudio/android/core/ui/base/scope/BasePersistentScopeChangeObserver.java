package ru.surfstudio.android.core.ui.base.scope;


import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.completely.destroy.OnCompletelyDestroyDelegate;

public class BasePersistentScopeChangeObserver implements OnCompletelyDestroyDelegate {

    PersistentScope persistentScope;

    public BasePersistentScopeChangeObserver(PersistentScope persistentScope) {
        this.persistentScope = persistentScope;
    }

    @Override
    public void onCompletelyDestroy() {
        persistentScope.setDestroyed(true);
    }
}
