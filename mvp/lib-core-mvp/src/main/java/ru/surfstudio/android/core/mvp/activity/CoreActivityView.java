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
package ru.surfstudio.android.core.mvp.activity;

import ru.surfstudio.android.core.mvp.delegate.ActivityViewDelegate;
import ru.surfstudio.android.core.mvp.delegate.factory.MvpScreenDelegateFactoryContainer;
import ru.surfstudio.android.core.mvp.presenter.CorePresenter;
import ru.surfstudio.android.core.mvp.scope.ActivityViewPersistentScope;
import ru.surfstudio.android.core.ui.activity.CoreActivity;

/**
 * Base class with core logic for view, based on {@link CoreActivity}
 */
public abstract class CoreActivityView extends CoreActivity implements
        CoreActivityViewInterface {

    protected abstract CorePresenter[] getPresenters();

    public abstract String getScreenName();

    @Override
    public ActivityViewDelegate createActivityDelegate() {
        return MvpScreenDelegateFactoryContainer.get().createActivityViewDelegate(this);
    }

    @Override
    public ActivityViewPersistentScope getPersistentScope() {
        return (ActivityViewPersistentScope) super.getPersistentScope();
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
