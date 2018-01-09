package ru.surfstudio.android.core.ui.base.placeholder;

import ru.surfstudio.android.core.ui.base.screen.model.state.LoadState;

/**
 * Интрефейс для вью отрисовывающей состояние {@link LoadState}
 */
public interface PlaceHolderView {

    void render(LoadState loadState);
}
