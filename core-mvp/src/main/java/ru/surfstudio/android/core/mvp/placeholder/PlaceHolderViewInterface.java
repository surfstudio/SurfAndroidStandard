package ru.surfstudio.android.core.mvp.placeholder;

import ru.surfstudio.android.core.mvp.model.state.LoadState;

/**
 * Интрефейс для вью отрисовывающей состояние {@link LoadState}
 */
public interface PlaceHolderViewInterface {
    void render(LoadState loadState);
}