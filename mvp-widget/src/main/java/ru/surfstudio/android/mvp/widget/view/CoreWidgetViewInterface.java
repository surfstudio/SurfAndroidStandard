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
 * <b/>При использовании в RecyclerView необходимо проставить атрибут enableManualInit == true в макете,
 * вызвать метод init(scopeId) во время bind()
 * <p>
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
     * Метод, используемый для получения уникального идентификатора виджета на экране.
     * Стандартная реализация извлекает id из layout, если он был там задан.
     * Следует переопределять, если задание уникального id из layout невозможно.
     *
     * @return уникальный идентификатор виджета на экране
     */
    String getWidgetId();

    /**
     * Для отложенной инициализации виджета с установкой идентификатора scope на основе данных
     * Используется, если у виджета проставлен атрибут - enableManualInit == true
     *
     * Для динамически создаваемых виджетов, необходимо переопределить метод getWidgetId,
     * чтобы виджет получал уникальные данные (например, data из viewHolder в RecyclerView),
     * и возвращал идентификатор на основе этих данных в getWidgetId.
     */
    void lazyInit();

    void onCreate();

    /**
     * Устрарел, для отложенной инициализации используется lazyInit
     */
    @Deprecated
    void init();

    /**
     * Устрарел, для отложенной инициализации используется lazyInit
     */
    @Deprecated
    void init(String scopeId);
}
