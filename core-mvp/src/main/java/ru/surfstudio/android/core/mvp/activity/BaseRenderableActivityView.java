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

import ru.surfstudio.android.core.mvp.model.ScreenModel;
import ru.surfstudio.android.core.mvp.view.RenderableView;

/**
 * базовый коасс для ActivityView, поддрерживающий отрисовку и обработку ошибок из презентера
 * и предоставляет стандартную обработку ошибок, для изменения логики обработки можно переопределить
 *
 * @param <M> модель, используемая для отрисовки см {@link ScreenModel}
 */
public abstract class BaseRenderableActivityView<M extends ScreenModel> extends CoreActivityView
        implements RenderableView<M> {

    protected abstract void renderInternal(M sm);

    @Override
    public void render(M sm) {
        renderInternal(sm);
    }

}
