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

import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface;
import ru.surfstudio.android.core.mvp.loadstate.LoadStateRendererInterface;
import ru.surfstudio.android.core.mvp.model.LdsScreenModel;

/**
 * Базовый класс FragmentView c поддержкой
 * состояния загрузки {@link LoadStateInterface}
 *
 * @param <M>
 */
public abstract class BaseLdsFragmentView<M extends LdsScreenModel>
        extends BaseRenderableFragmentView<M> {

    protected abstract LoadStateRendererInterface getLoadStateRenderer();

    @Override
    public void render(M sm) {
        renderLoadState(sm.getLoadState());
        renderInternal(sm);
    }

    protected void renderLoadState(LoadStateInterface loadState) {
        getLoadStateRenderer().render(loadState);
    }
}