package ru.surfstudio.android.core.ui.base.screen.presenter;


import java.io.Serializable;

//todo коммент
public interface StateRestorer<D extends Serializable> {

    D getCurrentState();

    void restoreState(D data);
}
