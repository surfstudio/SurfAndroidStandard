package ru.surfstudio.android.core.mvp.fragment;

import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator;
import ru.surfstudio.android.core.mvp.delegate.FragmentViewDelegate;
import ru.surfstudio.android.core.mvp.scope.FragmentViewPersistentScope;
import ru.surfstudio.android.core.mvp.view.PresenterHolderCoreView;
import ru.surfstudio.android.core.ui.fragment.CoreFragmentInterface;

/**
 * инрефейс для вью, основанной на фрагменте
 */
public interface CoreFragmentViewInterface extends PresenterHolderCoreView, CoreFragmentInterface {

    @Override
    BaseFragmentViewConfigurator createConfigurator();

    @Override
    FragmentViewPersistentScope getPersistentScope();

    @Override
    FragmentViewDelegate createFragmentDelegate();

}
