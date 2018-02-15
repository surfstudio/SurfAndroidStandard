package ru.surfstudio.android.core.ui.base.screen.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentConfigurator;
import ru.surfstudio.android.core.ui.base.screen.configurator.HasConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.fragment.FragmentDelegate;

//todo comment
public interface CoreFragmentInterface extends HasName, HasConfigurator {

    @Override
    BaseFragmentConfigurator createConfigurator();

    @Override
    BaseFragmentConfigurator getConfigurator();

    FragmentDelegate createFragmentDelegate();

    /**
     * @param viewRecreated show whether view created in first time or recreated after
     *                      changing configuration
     */
    void onActivityCreated(@Nullable Bundle savedInstanceState, boolean viewRecreated);
}
