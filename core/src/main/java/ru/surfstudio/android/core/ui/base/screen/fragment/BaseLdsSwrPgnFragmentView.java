package ru.surfstudio.android.core.ui.base.screen.fragment;


import ru.surfstudio.android.core.ui.base.recycler.BasePaginationableAdapter;
import ru.surfstudio.android.core.ui.base.screen.model.LdsSwrPgnScreenModel;
import ru.surfstudio.android.core.ui.base.screen.model.state.LoadState;
import ru.surfstudio.android.core.ui.base.screen.model.state.PaginationState;
import ru.surfstudio.android.core.ui.base.screen.model.state.SwipeRefreshState;

/**
 * базовый класс FragmentView c поддержкой
 * состояния загрузки {@link LoadState}
 * состояния SwipeRefresh {@link SwipeRefreshState}
 * состояния пагинации {@link PaginationState}
 * @param <M>
 */
public abstract class BaseLdsSwrPgnFragmentView<M extends LdsSwrPgnScreenModel>
        extends BaseLdsSwrFragmentView<M> {

    protected abstract BasePaginationableAdapter getPaginationableAdapter();

    @Override
    public void render(M screenModel) {
        renderLoadState(screenModel.getLoadState());
        renderSwipeRefreshState(screenModel.getSwipeRefreshState());
        renderPaginationState(screenModel.getPaginationState());
        renderInternal(screenModel);
    }

    protected void renderPaginationState(PaginationState paginationState) {
        getPaginationableAdapter().setState(paginationState);
    }
}
