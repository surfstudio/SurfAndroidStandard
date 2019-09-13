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
package ru.surfstudio.android.core.mvp.fragment;

import android.os.Bundle;

import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator;
import ru.surfstudio.android.core.mvp.delegate.FragmentViewDelegate;
import ru.surfstudio.android.core.mvp.delegate.factory.MvpScreenDelegateFactoryContainer;
import ru.surfstudio.android.core.mvp.presenter.CorePresenter;
import ru.surfstudio.android.core.mvp.scope.FragmentViewPersistentScope;
import ru.surfstudio.android.core.ui.fragment.CoreFragment;

/**
 * Base class with core logic for view, based on {@link CoreFragment}
 */
public abstract class CoreFragmentView extends CoreFragment
        implements CoreFragmentViewInterface {

    protected abstract CorePresenter[] getPresenters();

    @Override
    public abstract BaseFragmentViewConfigurator createConfigurator();

    @Override
    public FragmentViewDelegate createFragmentDelegate() {
        return MvpScreenDelegateFactoryContainer.get().createFragmentViewDelegate(this);
    }

    @Override
    public FragmentViewPersistentScope getPersistentScope() {
        return (FragmentViewPersistentScope) super.getPersistentScope();
    }

    /**
     * Override this instead {@link #onActivityCreated(Bundle)}
     *
     * @param viewRecreated showSimpleDialog whether view created in first time or recreated after
     *                      changing configuration
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState, boolean viewRecreated) {

    }

    /**
     * Bind presenter to this view
     * You can override this method for support different presenters for different views
     */
    @Override
    public void bindPresenters() {
        for (CorePresenter presenter : getPresenters()) {
            presenter.attachView(this);
        }
    }
}
