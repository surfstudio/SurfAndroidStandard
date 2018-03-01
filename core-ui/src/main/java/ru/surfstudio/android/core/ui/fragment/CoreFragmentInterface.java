package ru.surfstudio.android.core.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.core.ui.configurator.BaseFragmentConfigurator;
import ru.surfstudio.android.core.ui.configurator.HasConfigurator;
import ru.surfstudio.android.core.ui.delegate.fragment.FragmentDelegate;
import ru.surfstudio.android.core.ui.scope.FragmentPersistentScope;
import ru.surfstudio.android.core.ui.scope.HasPersistentScope;

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
