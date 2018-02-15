package ru.surfstudio.android.core.ui.base.screen.fragment;

import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.fragment.FragmentViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderCoreView;

/**
 * инрефейс для вью, которая оповещает презентер о событиях жизненного цикла экрана
 */
public interface CoreFragmentViewInterface extends PresenterHolderCoreView, CoreFragmentInterface {

    @Override
    BaseFragmentViewConfigurator createConfigurator();

    @Override
    BaseFragmentViewConfigurator getConfigurator();

    @Override
    FragmentViewDelegate createFragmentDelegate();

}
