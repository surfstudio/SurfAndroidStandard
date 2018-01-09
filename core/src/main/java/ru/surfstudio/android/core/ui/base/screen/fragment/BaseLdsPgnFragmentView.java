package ru.surfstudio.android.core.ui.base.screen.fragment;


import ru.surfstudio.android.core.ui.base.recycler.BasePaginationableAdapter;
import ru.surfstudio.android.core.ui.base.screen.model.LdsPgnScreenModel;
import ru.surfstudio.android.core.ui.base.screen.model.state.LoadState;
import ru.surfstudio.android.core.ui.base.screen.model.state.PaginationState;

/**
 * базовый класс FragmentView c поддержкой
 * состояния загрузки {@link LoadState}
 * состояния пагинации {@link PaginationState}
 *
 * @param <M>
 */
public abstract class BaseLdsPgnFragmentView<M extends LdsPgnScreenModel>
        extends BaseLdsFragmentView<M> {

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
