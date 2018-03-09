package ru.surfstudio.android.core.ui.delegate.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.ui.activity.CoreActivity;
import ru.surfstudio.android.core.ui.fragment.CoreFragmentInterface;
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope;

/**
 * ищет скоуп родительской активити для фрагмента
 */

public class ParentActivityPersistentScopeFinder {
    private Fragment childFragment;
    private PersistentScopeStorage scopeStorage;

    public <V extends Fragment & CoreFragmentInterface> ParentActivityPersistentScopeFinder(V childFragment, PersistentScopeStorage scopeStorage) {
        this.childFragment = childFragment;
        this.scopeStorage = scopeStorage;
    }

    public ActivityPersistentScope find() {
        ActivityPersistentScope parentScope = null;
        FragmentActivity activity = (FragmentActivity) childFragment.getContext();
        parentScope = ((CoreActivity) activity).getPersistentScope();
        return parentScope;
    }
}
