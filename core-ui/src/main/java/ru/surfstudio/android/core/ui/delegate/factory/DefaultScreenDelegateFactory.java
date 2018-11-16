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
package ru.surfstudio.android.core.ui.delegate.factory;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.List;

import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.delegate.activity.ActivityCompletelyDestroyChecker;
import ru.surfstudio.android.core.ui.delegate.activity.ActivityDelegate;
import ru.surfstudio.android.core.ui.delegate.fragment.FragmentCompletelyDestroyChecker;
import ru.surfstudio.android.core.ui.delegate.fragment.FragmentDelegate;
import ru.surfstudio.android.core.ui.event.ScreenEventResolverHelper;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.fragment.CoreFragmentInterface;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorageContainer;

/**
 * Фабрика делегатов экранов по умолчанию, предоставляет стандартные делегаты
 */
public class DefaultScreenDelegateFactory implements ScreenDelegateFactory {

    @Override
    public <A extends FragmentActivity & CoreActivityInterface> ActivityDelegate createActivityDelegate(A activity) {
        return new ActivityDelegate(
                activity,
                getScopeStorage(),
                getEventResolvers(),
                new ActivityCompletelyDestroyChecker(activity)
        );
    }

    @Override
    public <A extends Fragment & CoreFragmentInterface> FragmentDelegate createFragmentDelegate(A fragment) {
        return new FragmentDelegate(
                fragment,
                getScopeStorage(),
                getEventResolvers(),
                new FragmentCompletelyDestroyChecker(fragment)
        );
    }

    @NonNull
    protected List<ScreenEventResolver> getEventResolvers() {
        return ScreenEventResolverHelper.standardEventResolvers();
    }

    @NonNull
    protected PersistentScopeStorage getScopeStorage() {
        return PersistentScopeStorageContainer.getPersistentScopeStorage();
    }
}
