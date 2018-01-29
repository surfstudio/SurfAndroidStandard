package ru.surfstudio.android.core.ui.base.screen.delegate.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewParent;

import java.util.List;

import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentInterface;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.base.screen.widjet.CoreWidgetViewInterface;

/**
 * Created by makstuev on 28.01.2018.
 */

public class ParentPersistentScopeFinder {

    private View child;
    private PersistentScopeStorage scopeStorage;

    public <V extends View & CoreWidgetViewInterface> ParentPersistentScopeFinder(V child, PersistentScopeStorage scopeStorage) {
        this.child = child;
        this.scopeStorage = scopeStorage;
    }

    public PersistentScope find() {
        PersistentScope parentScope = null;
        FragmentActivity activity = (FragmentActivity) child.getContext();
        List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
        ViewParent parent = child.getParent();
        while (parent != null) {
            for (Fragment fragment : fragments) {
                if (fragment.getView() != null
                        && fragment.getView() == parent
                        && fragment instanceof CoreFragmentInterface) {
                    parentScope = scopeStorage.getByName(
                            ((CoreFragmentInterface) fragment).getConfigurator().getName());

                }
            }
            parent = child.getParent();
        }
        if (parentScope == null) {
            parentScope = scopeStorage.getActivityScope();
        }
        if (parentScope == null) {
            throw new IllegalStateException("WidgetView must be child of CoreActivityInterface or CoreFragmentInterface");
        }
        return parentScope;
    }
}
