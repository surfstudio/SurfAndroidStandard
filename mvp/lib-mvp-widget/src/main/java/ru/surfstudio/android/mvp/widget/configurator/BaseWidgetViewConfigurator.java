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
package ru.surfstudio.android.mvp.widget.configurator;

import android.view.View;

import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent;
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent;
import ru.surfstudio.android.core.mvp.configurator.ViewConfigurator;
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope;
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface;

/**
 * Базовый класс конфигуратора экрана, основанного на android.View, см {@link ViewConfigurator}
 *
 * @param <P> родительский даггер компонент (ActivityComponent)
 * @param <M> даггер модуль для виджета
 */
public abstract class BaseWidgetViewConfigurator<P, M> implements ViewConfigurator {

    private WidgetViewPersistentScope persistentScreenScope;
    private ScreenComponent component;

    public BaseWidgetViewConfigurator() {
    }

    protected abstract M getWidgetScreenModule();

    protected abstract P getParentComponent();

    protected abstract ScreenComponent createScreenComponent(P parentComponent,
                                                             M widgetScreenModule);
    protected <T extends View & CoreWidgetViewInterface> T getTargetWidgetView() {
        return  (T) getPersistentScope().getScreenState().getCoreWidget();
    }

    protected WidgetViewPersistentScope getPersistentScope() {
        return persistentScreenScope;
    }

    public void setPersistentScope(WidgetViewPersistentScope persistentScreenScope) {
        this.persistentScreenScope = persistentScreenScope;
    }

    @Override
    public void run() {
        satisfyDependencies(getPersistentScope().getScreenState().getCoreWidget());
    }

    @Override
    public ScreenComponent getScreenComponent() {
        return component;
    }

    private void satisfyDependencies(CoreWidgetViewInterface target) {
        if (component == null) {
            component = createScreenComponent();
        }
        component.inject(target);

        if (component instanceof BindableScreenComponent) {
            ((BindableScreenComponent) component).requestInjection();
        }
    }

    private ScreenComponent createScreenComponent() {
        return createScreenComponent(
                getParentComponent(),
                getWidgetScreenModule());
    }
}
