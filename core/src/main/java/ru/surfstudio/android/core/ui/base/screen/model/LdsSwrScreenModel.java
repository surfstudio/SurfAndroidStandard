package ru.surfstudio.android.core.ui.base.screen.model;


import ru.surfstudio.android.core.ui.base.screen.model.state.LoadState;
import ru.surfstudio.android.core.ui.base.screen.model.state.SwipeRefreshState;

/**
 * модель экрана с поддержкой
 * {@link LoadState}
 * {@link SwipeRefreshState}
 *
 * работает в связке c BaseLdsSwr...View
 * В случае изменения LoadState, SwipeRefreshState устанавливается в SwipeRefreshState.HIDE
 *
 * также см {@link ScreenModel}
 */
public class LdsSwrScreenModel extends LdsScreenModel {
    private SwipeRefreshState swipeRefreshState = SwipeRefreshState.HIDE;

    public SwipeRefreshState getSwipeRefreshState() {
        return swipeRefreshState;
    }

    public void setSwipeRefreshState(SwipeRefreshState swipeRefreshState) {
        this.swipeRefreshState = swipeRefreshState;
    }

    @Override
    public void setLoadState(LoadState loadState) {
        //В случае изменения LoadState, SwipeRefreshState устанавливается в SwipeRefreshState.HIDE
        setSwipeRefreshState(SwipeRefreshState.HIDE);
        super.setLoadState(loadState);
    }
}
