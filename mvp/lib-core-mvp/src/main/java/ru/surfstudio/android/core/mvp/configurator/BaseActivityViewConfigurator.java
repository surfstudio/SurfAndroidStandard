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

import android.app.Activity;
import android.content.Intent;

import ru.surfstudio.android.core.mvp.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.mvp.scope.ActivityViewPersistentScope;
import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope;

/**
 * Базовый класс конфигуратора экрана, основанного на Activity, см {@link ViewConfigurator}
 *
 * @param <P> родительский корневой даггер компонент
 * @param <M> даггер модуль для активити
 * @param <A> родительский ActivityComponent
 */
public abstract class BaseActivityViewConfigurator<P, A, M>
        extends BaseActivityConfigurator<A, P>
        implements ViewConfigurator {

    private Intent intent;
    private ScreenComponent screenComponent;
    private ActivityViewPersistentScope persistentScope;


    public BaseActivityViewConfigurator(Intent intent) {
        this.intent = intent;
    }

    protected abstract ScreenComponent createScreenComponent(A parentActivityComponent,
                                                             M activityScreenModule,
                                                             Intent intent);

    protected abstract M getActivityScreenModule();

    @Override
    public void run() {
        super.run();
        satisfyDependencies(getTargetActivityView());
    }

    protected <T extends Activity & CoreActivityViewInterface> T getTargetActivityView() {
        return (T) getPersistentScope().getScreenState().getCoreActivityView();
    }

    @Override
    public ScreenComponent getScreenComponent() {
        return screenComponent;
    }

    private void satisfyDependencies(CoreActivityViewInterface target) {
        if (screenComponent == null) {
            screenComponent = createScreenComponent();
        }
        screenComponent.inject(target);

        if (screenComponent instanceof BindableScreenComponent) {
            ((BindableScreenComponent) screenComponent).requestInjection();
        }
    }

    protected Intent getIntent() {
        return intent;
    }

    private ScreenComponent createScreenComponent() {
        return createScreenComponent(
                getActivityComponent(),
                getActivityScreenModule(),
                getIntent()
        );
    }

    @Override
    protected ActivityViewPersistentScope getPersistentScope() {
        return persistentScope;
    }

    @Override
    @Deprecated
    public void setPersistentScope(ActivityPersistentScope persistentScreenScope) {
        throw new UnsupportedOperationException("call another setPersistentScope");
    }

    public void setPersistentScope(ActivityViewPersistentScope persistentScope) {
        super.setPersistentScope(persistentScope);
        this.persistentScope = persistentScope;
    }
}
