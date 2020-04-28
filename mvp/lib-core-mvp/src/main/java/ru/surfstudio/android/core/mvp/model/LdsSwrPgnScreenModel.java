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
package ru.surfstudio.android.core.mvp.model;

import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface;
import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState;
import ru.surfstudio.android.easyadapter.pagination.PaginationState;

/**
 * модель экрана с поддержкой
 * {@link LoadStateInterface}
 * {@link SwipeRefreshState}
 * {@link PaginationState}
 * <p>
 * работает в связке c BaseLdsSwrPgn...View
 * В случае изменения LoadState, SwipeRefreshState устанавливается в SwipeRefreshState.HIDE
 * <p>
 * также см {@link ScreenModel}
 */
public class LdsSwrPgnScreenModel extends LdsSwrScreenModel {
    private PaginationState paginationState = PaginationState.COMPLETE;

    public PaginationState getPaginationState() {
        return paginationState;
    }

    public void setPaginationState(PaginationState paginationState) {
        this.paginationState = paginationState;
    }

    public void setNormalPaginationState(boolean canLoadMore) {
        setPaginationState(canLoadMore ? PaginationState.READY : PaginationState.COMPLETE);
    }

    public void setErrorPaginationState() {
        setPaginationState(PaginationState.ERROR);
    }
}
