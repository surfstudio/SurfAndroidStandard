package ru.surfstudio.android.core.mvp.configurator;

import ru.surfstudio.android.core.mvp.presenter.Presenter;
import ru.surfstudio.android.core.mvp.view.PresenterHolderCoreView;

/**
 * Компонент для экранов с биндингом
 */
public interface BindableScreenComponent<V extends PresenterHolderCoreView> extends ScreenComponent<V> {

    Presenter[] initPresenter();
}
