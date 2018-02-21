package ru.surfstudio.android.core.mvp.activity;


import ru.surfstudio.android.core.mvp.error.ErrorHandler;
import ru.surfstudio.android.core.mvp.model.ScreenModel;
import ru.surfstudio.android.core.mvp.view.RenderableView;

/**
 * базовый коасс для ActivityView, поддрерживающий отрисовку и обработку ошибок из презентера
 * и предоставляет стандартную обработку ошибок, для изменения логики обработки можно переопределить
 * {@link #handleError(Throwable)} или {@link #getErrorHandler()}
 *
 * @param <M> модель, используемая для отрисовки см {@link ScreenModel}
 */
public abstract class BaseRenderableHandleableErrorActivityView<M extends ScreenModel> extends BaseHandleableErrorActivityView
        implements RenderableView<M> {

    public BaseRenderableHandleableErrorActivityView(ErrorHandler standardErrorHandler) {
        super(standardErrorHandler);
    }

    protected abstract void renderInternal(M screenModel);

    @Override
    public void render(M screenModel) {
        renderInternal(screenModel);
    }

}
