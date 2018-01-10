package ru.surfstudio.standard.app.dagger;

import android.content.Context;

import dagger.Component;
import ru.surfstudio.android.core.app.bus.RxBus;
import ru.surfstudio.android.core.app.dagger.scope.PerActivity;
import ru.surfstudio.android.core.app.scheduler.SchedulersProvider;

@PerActivity
@Component(dependencies = AppComponent.class)
public interface ActivityComponent {
    Context context();
    SchedulersProvider schedulerProvider();
    RxBus rxBus();
}
