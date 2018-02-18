package ru.surfstudio.android.core.mvp.view;


import ru.surfstudio.android.core.mvp.model.ScreenModel;

/**
 * интерфейс для вью, поддерживающей отрисовку модели экрана
 *
 * @param <M>
 */
public interface RenderableView<M extends ScreenModel> {
    void render(M screenModel);
}
