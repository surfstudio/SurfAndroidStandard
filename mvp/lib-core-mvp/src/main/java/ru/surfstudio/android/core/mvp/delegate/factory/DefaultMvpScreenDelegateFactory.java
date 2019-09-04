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
package ru.surfstudio.android.core.mvp.delegate.factory;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.List;

import ru.surfstudio.android.core.mvp.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.mvp.delegate.ActivityViewDelegate;
import ru.surfstudio.android.core.mvp.delegate.FragmentViewDelegate;
import ru.surfstudio.android.core.mvp.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.core.ui.delegate.activity.ActivityCompletelyDestroyChecker;
import ru.surfstudio.android.core.ui.delegate.fragment.FragmentCompletelyDestroyChecker;
import ru.surfstudio.android.core.ui.event.ScreenEventResolverHelper;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorageContainer;


/**
 * Фабрика делегатов MVP экранов по умолчанию, предоставляет стандартные делегаты
 */
public class DefaultMvpScreenDelegateFactory implements MvpScreenDelegateFactory {

    @Override
    public <A extends FragmentActivity & CoreActivityViewInterface> ActivityViewDelegate createActivityViewDelegate(A activity) {
        return new ActivityViewDelegate(
                activity,
                getScopeStorage(),
                getEventResolvers(),
                new ActivityCompletelyDestroyChecker(activity)
        );
    }

    @Override
    public <A extends Fragment & CoreFragmentViewInterface> FragmentViewDelegate createFragmentViewDelegate(A fragment) {
        return new FragmentViewDelegate(
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
