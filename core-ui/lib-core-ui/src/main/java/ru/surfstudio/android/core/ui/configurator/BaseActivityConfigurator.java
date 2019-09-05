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
package ru.surfstudio.android.core.ui.configurator;

import android.app.Activity;

import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope;

/**
 * Базовый конфигуратор для активити
 * Создает ActivityComponent
 * Предоставляет уникальное имя экрана, для корневой логики экрана
 *
 * @param <A> тип ActivityComponent
 * @param <P> тип родительского компонента, обычно AppComponent
 */
public abstract class BaseActivityConfigurator<A, P> implements Configurator {
    private ActivityPersistentScope persistentScreenScope;
    private A activityComponent;

    protected abstract A createActivityComponent(P parentComponent);

    protected abstract P getParentComponent();

    @Override
    public void run() {
        init();
    }

    protected ActivityPersistentScope getPersistentScope() {
        return persistentScreenScope;
    }

    protected void init() {
        if (activityComponent == null) {
            activityComponent = createActivityComponent(getParentComponent());
        }
    }

    public A getActivityComponent() {
        return activityComponent;
    }

    public void setPersistentScope(ActivityPersistentScope persistentScreenScope) {
        this.persistentScreenScope = persistentScreenScope;
    }

    protected <T extends Activity & CoreActivityInterface> T getTargetActivity() {
        return (T) getPersistentScope().getScreenState().getCoreActivity();
    }

}
