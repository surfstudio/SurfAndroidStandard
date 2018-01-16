package ru.surfstudio.android.core.app.dagger;

import dagger.Module;
import ru.surfstudio.android.core.app.SharedPrefModule;
import ru.surfstudio.android.core.app.intialization.InitializationModule;
import ru.surfstudio.android.core.app.scheduler.SchedulerModule;

/**
 * Представялет основные зависимости ядра
 */

@Module(includes = {
        InitializationModule.class,
        SharedPrefModule.class,
        SchedulerModule.class
})
public interface CoreAppModule {
}
