package ru.surfstudio.android.core.ui.base.screen.presenter;


import java.io.Serializable;

/**
 * Сохраняет и восстанавливает состояние презетера при восстановлении экрана с диска
 * @param <D>
 */
public interface StateRestorer<D extends Serializable> {

    D getCurrentState();

    void restoreState(D data);
}
