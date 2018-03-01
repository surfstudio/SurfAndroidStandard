package ru.surfstudio.android.core.mvp.fragment;


import ru.surfstudio.android.core.mvp.model.ScreenModel;
import ru.surfstudio.android.core.mvp.view.RenderableView;

/**
 * базовый класс для FragmentView, поддрерживающий отрисовку и обработку ошибок из презентера
 *
 * @param <M> модель, используемая для отрисовки см {@link ScreenModel}
 */
public abstract class BaseRenderableFragmentView<M extends ScreenModel> extends CoreFragmentView
        implements RenderableView<M> {

    protected abstract void renderInternal(M screenModel);

    @Override
    public void render(M screenModel) {
        renderInternal(screenModel);
    }

}
