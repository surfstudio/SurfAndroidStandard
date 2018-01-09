package ru.surfstudio.android.core.app.scheduler;


import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.app.dagger.scope.PerApplication;

@Module
public class SchedulerModule {

    @Provides
    @PerApplication
    public SchedulersProvider provideSchedulerProvider(){
        return new SchedulersProviderImpl();
    }
}
