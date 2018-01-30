package ru.surfstudio.android.core.ui.base.dagger;

import android.content.Context;

import ru.surfstudio.android.core.app.bus.RxBus;
import ru.surfstudio.android.core.app.connection.ConnectionProvider;
import ru.surfstudio.android.core.app.scheduler.SchedulersProvider;
import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.screen.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.state.ActivityScreenState;

/**
 * Created by makstuev on 29.01.2018.
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
