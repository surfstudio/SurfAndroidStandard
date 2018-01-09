package ru.surfstudio.android.core.ui.base.screen.fragment;


import ru.surfstudio.android.core.ui.base.placeholder.PlaceHolderView;
import ru.surfstudio.android.core.ui.base.screen.model.LdsScreenModel;
import ru.surfstudio.android.core.ui.base.screen.model.state.LoadState;

/**
 * базовый класс FragmentView c поддержкой
 * состояния загрузки {@link LoadState}
 * @param <M>
 */
public abstract class BaseLdsFragmentView<M extends LdsScreenModel>
        extends BaseRenderableHandleableErrorFragmentView<M> {

    protected abstract PlaceHolderView getPlaceHolderView();

    @Override
    public void render(M screenModel) {
        renderLoadState(screenModel.getLoadState());
        renderInternal(screenModel);
    }

    protected void renderLoadState(LoadState loadState) {
        getPlaceHolderView().render(loadState);
    }
}
