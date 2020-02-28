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
 * Для использования виджетов в RecyclerView, необходимо переопределить метод getWidgetId так,
 * чтобы он получал значение из данных, получаемых в методе bind() у ViewHolder.
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
     * <p>
     * Необходимо использовать только в крайних случаях, когда не устраивает автоматическая инициализация на onAttachedToWindow.
     * Это может потребоваться в случае когда onAttachedToWindow вызывается до того, как становится известен widgetId.
     * <p>
     * Не рекомендуется использовать атрибут enableManualInit в связке с lazyInit
     * для виджетов внутри RecyclerView. Для идентификации элементов в RecyclerView, необходимо переопределять метод {@link CoreWidgetViewInterface#getWidgetId}.
     */
    void lazyInit();

    void onCreate();
}
