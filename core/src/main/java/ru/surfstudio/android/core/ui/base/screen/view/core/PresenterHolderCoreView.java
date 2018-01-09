package ru.surfstudio.android.core.ui.base.screen.view.core;

import com.agna.ferro.core.HasName;

import ru.surfstudio.android.core.ui.base.screen.configurator.HasScreenConfigurator;
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter;

/**
 *  инрефейс для вью, которая оповещает презентер о событиях жизненного цикла экрана
 */
public interface PresenterHolderCoreView extends CoreView, HasName, HasScreenConfigurator {

    CorePresenter[] getPresenters();

    void bindPresenters();

}
