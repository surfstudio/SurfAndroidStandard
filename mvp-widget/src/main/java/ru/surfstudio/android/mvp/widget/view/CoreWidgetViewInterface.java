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
package ru.surfstudio.android.mvp.widget.view;

import ru.surfstudio.android.core.mvp.view.PresenterHolderCoreView;
import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.core.ui.configurator.HasConfigurator;
import ru.surfstudio.android.core.ui.scope.HasPersistentScope;
import ru.surfstudio.android.mvp.widget.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.android.mvp.widget.delegate.WidgetViewDelegate;
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope;

/**
 * Интерфейс для всех кастомных вьюшек с презентером
 * <p>
 * !!!ВАЖНО!!!
 * 1) Необходимо вызвать метод init во время onCreate() Activity или onActivityCreated() Fragment
 * 2) кастомная вьюшка с презентером может быть только в статической иерархии вью,
 * то есть должна создаваться при старте экрана, и не может быть использована при
 * динамическом создании вью, в том числе внутри элементов RecyclerView
 * <p>
 * Эти ограничения связаны с большими сложностями реализации делегирования событий экрана
 * для динамических вью
 */

public interface CoreWidgetViewInterface extends
        PresenterHolderCoreView,
        HasConfigurator,
        HasPersistentScope,
        HasName {

    @Override
    BaseWidgetViewConfigurator createConfigurator();

    @Override
    WidgetViewPersistentScope getPersistentScope();

    WidgetViewDelegate createWidgetViewDelegate();

    /**
     * Необходимо вызвать метод init во время Activity.onCreate() или Fragment.onActivityCreated()
     * или CoreWidgetView.onCreate()
     */
    void init();

    void onCreate();
}
