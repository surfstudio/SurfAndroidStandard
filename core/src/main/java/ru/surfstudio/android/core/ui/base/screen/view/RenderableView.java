package ru.surfstudio.android.core.ui.base.screen.view;


import ru.surfstudio.android.core.ui.base.screen.model.ScreenModel;

/**
 * интерфейс для вью, поддерживающей отрисовку модели экрана
 * @param <M>
 */
public interface RenderableView<M extends ScreenModel>{
    void render(M screenModel);
}
