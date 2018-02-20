package ru.surfstudio.android.core.ui.dagger;

import android.content.Context;

import ru.surfstudio.android.core.ui.bus.RxBus;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.state.ActivityScreenState;

/**
 * Базовый компонент для dagger Activity компонента,
 * пробрасывает необходимые сущности в дочерний компонент
 */

public interface BaseCoreActivityComponent {

    ActivityProvider activityProvider();

    ActivityPersistentScope activityPersistentScope();

    ActivityScreenState activityScreenState();

    RxBus rxBus();

    Context context();
}
