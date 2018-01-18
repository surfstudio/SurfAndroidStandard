package ru.surfstudio.android.core.app.dagger;

import dagger.Module;
import ru.surfstudio.android.core.app.SharedPrefModule;
import ru.surfstudio.android.core.app.connection.ConnectionModule;
import ru.surfstudio.android.core.app.interactor.common.network.OkHttpModule;
import ru.surfstudio.android.core.app.intialization.InitializationModule;
import ru.surfstudio.android.core.app.scheduler.SchedulerModule;

/**
 * Представялет основные зависимости ядра
 */

@Module(includes = {
        OkHttpModule.class,
        InitializationModule.class,
        SharedPrefModule.class,
        SchedulerModule.class,
        ConnectionModule.class
})
public interface CoreAppModule {
}
