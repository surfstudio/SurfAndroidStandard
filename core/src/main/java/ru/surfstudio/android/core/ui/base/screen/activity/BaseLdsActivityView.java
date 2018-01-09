package ru.surfstudio.android.core.ui.base.screen.activity;


import ru.surfstudio.android.core.ui.base.placeholder.PlaceHolderView;
import ru.surfstudio.android.core.ui.base.screen.model.LdsScreenModel;
import ru.surfstudio.android.core.ui.base.screen.model.state.LoadState;

/**
 * базовый класс ActivityView c поддержкой
 * состояния загрузки {@link LoadState}
 * Используется вместе с PlaceHolderView.
 * @param <M>
 */
public abstract class BaseLdsActivityView<M extends LdsScreenModel>
        extends BaseRenderableHandleableErrorActivityView<M> {

    protected abstract PlaceHolderView getPlaceHolderView();

    @Override
    public void render(M screenModel) {
        renderLoadState(screenModel.getLoadState());
        renderInternal(screenModel);
    }

    public void renderLoadState(LoadState loadState) {
        getPlaceHolderView().render(loadState);
    }
}
