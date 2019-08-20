/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

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
package ru.surfstudio.android.core.ui.delegate.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.fragment.CoreFragmentInterface;
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;

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
        parentScope = ((CoreActivityInterface) activity).getPersistentScope();
        return parentScope;
    }
}
