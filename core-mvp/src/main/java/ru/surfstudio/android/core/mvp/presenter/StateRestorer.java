package ru.surfstudio.android.core.mvp.presenter;


import java.io.Serializable;

/**
 * Сохраняет и восстанавливает состояние презетера при восстановлении экрана с диска
 *
 * @param <D>
 */
public interface StateRestorer<D extends Serializable> {

    D getCurrentState();

    void restoreState(D data);
}
