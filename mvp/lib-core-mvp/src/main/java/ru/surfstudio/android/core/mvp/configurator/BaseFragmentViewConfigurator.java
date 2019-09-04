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
package ru.surfstudio.android.core.mvp.configurator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import ru.surfstudio.android.core.mvp.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.core.mvp.scope.FragmentViewPersistentScope;
import ru.surfstudio.android.core.ui.configurator.BaseFragmentConfigurator;

/**
 * Базовый класс конфигуратора экрана, основанного на Fragment, см {@link ViewConfigurator}
 *
 * @param <P> родительский даггер компонент (ActivityComponent)
 * @param <M> даггер модуль для фрагмента
 */
public abstract class BaseFragmentViewConfigurator<P, M>
        extends BaseFragmentConfigurator
        implements ViewConfigurator {

    private Bundle args;
    private ScreenComponent screenComponent;
    private FragmentViewPersistentScope persistentScope;

    public BaseFragmentViewConfigurator(Bundle args) {
        this.args = args;
    }

    protected abstract M getFragmentScreenModule();

    protected abstract P getParentComponent();

    protected abstract ScreenComponent createScreenComponent(P parentComponent,
                                                             M fragmentScreenModule,
                                                             Bundle args);

    @Override
    public void run() {
        super.run();
        satisfyDependencies(getTargetFragmentView());
    }

    protected <T extends Fragment & CoreFragmentViewInterface> T getTargetFragmentView() {
        return (T) getPersistentScope().getScreenState().getCoreFragmentView();
    }

    @Override
    public ScreenComponent getScreenComponent() {
        return screenComponent;
    }

    private void satisfyDependencies(CoreFragmentViewInterface target) {
        if (screenComponent == null) {
            screenComponent = createScreenComponent();
        }
        screenComponent.inject(target);

        if (screenComponent instanceof BindableScreenComponent) {
            ((BindableScreenComponent) screenComponent).requestInjection();
        }
    }

    private ScreenComponent createScreenComponent() {
        return createScreenComponent(
                getParentComponent(),
                getFragmentScreenModule(),
                args);
    }

    protected Bundle getArgs() {
        return args;
    }

    protected FragmentViewPersistentScope getPersistentScope() {
        return persistentScope;
    }

    public void setPersistentScope(FragmentViewPersistentScope persistentScope) {
        this.persistentScope = persistentScope;
    }

}
