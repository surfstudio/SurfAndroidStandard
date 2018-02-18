package ru.surfstudio.android.core.mvp.fragment;


import ru.surfstudio.android.core.mvp.model.ScreenModel;
import ru.surfstudio.android.core.mvp.view.RenderableView;

/**
 * базовый класс для FragmentView, поддрерживающий отрисовку и обработку ошибок из презентера
 * предоставляет стандартную обработку ошибок, для изменения логики обработки можно переопределить
 * {@link #handleError(Throwable)} или {@link #getErrorHandler()}
 *
 * @param <M> модель, используемая для отрисовки см {@link ScreenModel}
 */
public abstract class BaseRenderableHandleableErrorFragmentView<M extends ScreenModel> extends BaseHandleableErrorFragmentView
        implements RenderableView<M> {

    protected abstract void renderInternal(M screenModel);

    @Override
    public void render(M screenModel) {
        renderInternal(screenModel);
    }

    @Override
    public void handleError(Throwable error) {
        getErrorHandler().handleError(error);
    }

}
