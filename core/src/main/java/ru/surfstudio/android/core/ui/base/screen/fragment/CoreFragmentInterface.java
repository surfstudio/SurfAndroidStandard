package ru.surfstudio.android.core.ui.base.screen.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentConfigurator;
import ru.surfstudio.android.core.ui.base.screen.configurator.HasConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.fragment.FragmentDelegate;
import ru.surfstudio.android.core.ui.base.screen.scope.FragmentPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.HasPersistentScope;

/**
 * Интерфес для базового фрагмента, см {@link FragmentDelegate}
 */
public interface CoreFragmentInterface extends
        HasConfigurator,
        HasPersistentScope,
        HasName {

    @Override
    BaseFragmentConfigurator createConfigurator();

    @Override
    FragmentPersistentScope getPersistentScope();

    FragmentDelegate createFragmentDelegate();

    /**
     * @param viewRecreated showSimpleDialog whether view created in first time or recreated after
     *                      changing configuration
     */
    void onActivityCreated(@Nullable Bundle savedInstanceState, boolean viewRecreated);
}
