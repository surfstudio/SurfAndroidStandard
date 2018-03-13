package ru.surfstudio.android.core.mvp.activity;


import android.support.v4.widget.SwipeRefreshLayout;

import ru.surfstudio.android.core.mvp.model.LdsSwrScreenModel;
import ru.surfstudio.android.core.mvp.model.state.LoadState;
import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState;

/**
 * базовый класс ActivityView c поддержкой
 * состояния загрузки {@link LoadState}
 * состояния SwipeRefresh {@link SwipeRefreshState}
 *
 * @param <M>
 */
public abstract class BaseLdsSwrActivityView<M extends LdsSwrScreenModel>
        extends BaseLdsActivityView<M> {

    protected abstract SwipeRefreshLayout getSwipeRefreshLayout();

    @Override
    public void render(M screenModel) {
        renderLoadState(screenModel.getLoadState());
        renderSwipeRefreshState(screenModel.getSwipeRefreshState());
        renderInternal(screenModel);
    }

    protected void renderSwipeRefreshState(SwipeRefreshState swipeRefreshState) {
        getSwipeRefreshLayout().setRefreshing(swipeRefreshState == SwipeRefreshState.REFRESHING);
    }
}
