package ru.surfstudio.android.easyadapter.screen;


import ru.surfstudio.android.core.ui.base.screen.activity.BaseLdsActivityView;
import ru.surfstudio.android.core.ui.base.screen.model.state.LoadState;
import ru.surfstudio.android.easyadapter.impl.pagination.BasePaginationableAdapter;
import ru.surfstudio.android.easyadapter.impl.pagination.PaginationState;
import ru.surfstudio.android.easyadapter.screen.model.LdsPgnScreenModel;

/**
 * базовый класс ActivityView c поддержкой
 * состояния загрузки {@link LoadState}
 * состояния пагинации {@link PaginationState}
 *
 * @param <M>
 */
public abstract class BaseLdsPgnActivityView<M extends LdsPgnScreenModel>
        extends BaseLdsActivityView<M> {

    protected abstract BasePaginationableAdapter getPaginationableAdapter();

    @Override
    public void render(M screenModel) {
        renderLoadState(screenModel.getLoadState());
        renderPaginationState(screenModel.getPaginationState());
        renderInternal(screenModel);
    }

    protected void renderPaginationState(PaginationState paginationState) {
        getPaginationableAdapter().setState(paginationState);
    }
}
