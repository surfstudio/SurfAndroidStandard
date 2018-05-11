/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.mvp.widget.delegate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewParent;

import java.util.List;

import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.fragment.CoreFragmentInterface;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope;
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface;

/**
 * ищет родительский PersistentScope для WidgetView
 */

public class ParentPersistentScopeFinder {

    private View child;
    private PersistentScopeStorage scopeStorage;

    public <V extends View & CoreWidgetViewInterface> ParentPersistentScopeFinder(V child, PersistentScopeStorage scopeStorage) {
        this.child = child;
        this.scopeStorage = scopeStorage;
    }

    public ScreenPersistentScope find() {
        ScreenPersistentScope parentScope = null;
        FragmentActivity activity = (FragmentActivity) child.getContext();
        List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
        ViewParent parent = child.getParent();
        while (parent != null && parentScope == null) {
            for (Fragment fragment : fragments) {
                if (fragment.getView() != null
                        && fragment.getView() == parent
                        && fragment instanceof CoreFragmentInterface) {
                    parentScope = ((CoreFragmentInterface) fragment).getPersistentScope();
                }
            }
            parent = parent.getParent();
        }
        if (parentScope == null) {
            parentScope = ((CoreActivityInterface)activity).getPersistentScope();
        }
        return parentScope;
    }
}
