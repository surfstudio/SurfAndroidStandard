package ru.surfstudio.android.core.ui.base.screen.model;


import ru.surfstudio.android.core.ui.base.screen.model.state.LoadState;

/**
 * модель экрана с поддержкой
 * {@link LoadState}
 *
 * работает в связке c BaseLds...View
 *
 * также см {@link ScreenModel}
 */
public class LdsScreenModel extends ScreenModel {
    private LoadState loadState = LoadState.UNSPECIFIED;

    public void setLoadState(LoadState loadState) {
        this.loadState = loadState;
    }

    public LoadState getLoadState() {
        return loadState;
    }
}
