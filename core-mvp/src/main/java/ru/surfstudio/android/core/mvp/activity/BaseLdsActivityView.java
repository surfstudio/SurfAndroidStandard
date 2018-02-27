package ru.surfstudio.android.core.mvp.activity;


import ru.surfstudio.android.core.mvp.model.LdsScreenModel;
import ru.surfstudio.android.core.mvp.model.state.LoadState;
import ru.surfstudio.android.core.mvp.placeholder.PlaceHolderView;

/**
 * базовый класс ActivityView c поддержкой
 * состояния загрузки {@link LoadState}
 * Используется вместе с PlaceHolderView.
 *
 * @param <M>
 */
public abstract class BaseLdsActivityView<M extends LdsScreenModel>
        extends BaseRenderableActivityView<M> {

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
