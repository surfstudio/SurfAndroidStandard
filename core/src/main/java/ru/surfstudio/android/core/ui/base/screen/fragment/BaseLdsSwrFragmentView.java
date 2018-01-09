package ru.surfstudio.android.core.ui.base.screen.fragment;


import android.support.v4.widget.SwipeRefreshLayout;

import ru.surfstudio.android.core.ui.base.screen.model.LdsSwrScreenModel;
import ru.surfstudio.android.core.ui.base.screen.model.state.LoadState;
import ru.surfstudio.android.core.ui.base.screen.model.state.SwipeRefreshState;

/**
 * базовый класс FragmentView c поддержкой
 * состояния загрузки {@link LoadState}
 * состояния SwipeRefresh {@link SwipeRefreshState}
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
