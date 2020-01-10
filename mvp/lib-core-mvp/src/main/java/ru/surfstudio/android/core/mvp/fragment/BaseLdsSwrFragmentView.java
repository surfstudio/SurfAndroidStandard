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

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface;
import ru.surfstudio.android.core.mvp.model.LdsSwrScreenModel;
import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState;

/**
 * базовый класс FragmentView c поддержкой
 * состояния загрузки {@link LoadStateInterface}
 * состояния SwipeRefresh {@link SwipeRefreshState}
 *
 * @param <M>
 */
public abstract class BaseLdsSwrFragmentView<M extends LdsSwrScreenModel>
        extends BaseLdsFragmentView<M> {

    protected abstract SwipeRefreshLayout getSwipeRefreshLayout();

    @Override
    public void render(M sm) {
        renderLoadState(sm.getLoadState());
        renderSwipeRefreshState(sm.getSwipeRefreshState());
        renderInternal(sm);
    }

    protected void renderSwipeRefreshState(SwipeRefreshState swipeRefreshState) {
        getSwipeRefreshLayout().setRefreshing(swipeRefreshState == SwipeRefreshState.REFRESHING);
    }
}
