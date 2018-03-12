package ru.surfstudio.android.core.mvp.model;


import ru.surfstudio.android.core.mvp.model.state.LoadState;

/**
 * модель экрана с поддержкой
 * {@link LoadState}
 * <p>
 * работает в связке c BaseLds...View
 * <p>
 * также см {@link ScreenModel}
 */
public class LdsScreenModel extends ScreenModel {
    private LoadState loadState = LoadState.NONE;

    public void setLoadState(LoadState loadState) {
        this.loadState = loadState;
    }

    public LoadState getLoadState() {
        return loadState;
    }
}
