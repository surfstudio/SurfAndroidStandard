package ru.surfstudio.android.core.mvp.fragment;


import android.support.v4.widget.SwipeRefreshLayout;

import ru.surfstudio.android.core.mvp.model.LdsSwrScreenModel;
import ru.surfstudio.android.core.mvp.model.state.LoadState;
import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState;

/**
 * базовый класс FragmentView c поддержкой
 * состояния загрузки {@link LoadState}
 * состояния SwipeRefresh {@link SwipeRefreshState}
 *
 * @param <M>
 */
public abstract class BaseLdsSwrFragmentView<M extends LdsSwrScreenModel>
        extends BaseLdsFragmentView<M> {

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
