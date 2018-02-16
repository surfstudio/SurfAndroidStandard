package ru.surfstudio.android.core.ui.base.screen.activity;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.core.ui.base.screen.configurator.HasConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.activity.ActivityDelegate;
import ru.surfstudio.android.core.ui.base.screen.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.HasPersistentScope;

/**
 * интерфейс базовой активити, см {@link ActivityDelegate}
 */
public interface CoreActivityInterface extends
        HasConfigurator,
        HasPersistentScope,
        HasName {

    @Override
    BaseActivityConfigurator createConfigurator();

    @Override
    ActivityPersistentScope getPersistentScope();

    ActivityDelegate createActivityDelegate();

    /**
     * @param viewRecreated render whether view created in first time or recreated after
     *                      changing configuration
     */
    void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState, boolean viewRecreated);
}
