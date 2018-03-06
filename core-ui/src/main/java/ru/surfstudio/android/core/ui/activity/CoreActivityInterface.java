package ru.surfstudio.android.core.ui.activity;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.core.ui.configurator.HasConfigurator;
import ru.surfstudio.android.core.ui.delegate.activity.ActivityDelegate;
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.scope.HasPersistentScope;

/**
 * интерфейс базовой активити, см {@link ActivityDelegate}
 */
public interface CoreActivityInterface extends
        HasConfigurator,
        HasPersistentScope {

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

    /**
     * Используется для только логирования (Может быть не уникальным)
     * @return возвращает имя для логгирования
     */
    String getScreenName();
}
