package ru.surfstudio.android.core.mvp.fragment;

import ru.surfstudio.android.core.mvp.model.LdsScreenModel;
import ru.surfstudio.android.core.mvp.model.state.LoadState;
import ru.surfstudio.android.core.mvp.placeholder.PlaceHolderViewInterface;

/**
 * базовый класс FragmentView c поддержкой
 * состояния загрузки {@link LoadState}
 *
 * @param <M>
 */
public abstract class BaseLdsFragmentView<M extends LdsScreenModel>
        extends BaseRenderableFragmentView<M> {

    protected abstract PlaceHolderViewInterface getPlaceHolderView();

    @Override
    public void render(M screenModel) {
        renderLoadState(screenModel.getLoadState());
        renderInternal(screenModel);
    }

    protected void renderLoadState(LoadState loadState) {
        getPlaceHolderView().render(loadState);
    }
}