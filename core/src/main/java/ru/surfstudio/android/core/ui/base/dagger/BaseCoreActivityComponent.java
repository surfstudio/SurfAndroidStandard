package ru.surfstudio.android.core.ui.base.dagger;

import android.content.Context;

import ru.surfstudio.android.connection.ConnectionProvider;
import ru.surfstudio.android.core.app.bus.RxBus;
import ru.surfstudio.android.core.app.scheduler.SchedulersProvider;
import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.screen.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.state.ActivityScreenState;

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

    SchedulersProvider schedulerProvider();

    ConnectionProvider connectionProvider();
}
