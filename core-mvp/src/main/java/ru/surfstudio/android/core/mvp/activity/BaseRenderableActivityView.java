package ru.surfstudio.android.core.mvp.activity;


import ru.surfstudio.android.core.mvp.model.ScreenModel;
import ru.surfstudio.android.core.mvp.view.RenderableView;

/**
 * базовый коасс для ActivityView, поддрерживающий отрисовку и обработку ошибок из презентера
 * и предоставляет стандартную обработку ошибок, для изменения логики обработки можно переопределить
 *
 * @param <M> модель, используемая для отрисовки см {@link ScreenModel}
 */
public abstract class BaseRenderableActivityView<M extends ScreenModel> extends CoreActivityView
        implements RenderableView<M> {

    protected abstract void renderInternal(M screenModel);

    @Override
    public void render(M screenModel) {
        renderInternal(screenModel);
    }

}
