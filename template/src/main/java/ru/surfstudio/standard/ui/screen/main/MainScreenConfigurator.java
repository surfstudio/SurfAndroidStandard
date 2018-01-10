package ru.surfstudio.standard.ui.screen.main;

import android.app.Activity;
import android.content.Intent;

import dagger.Component;
import ru.surfstudio.android.core.app.dagger.scope.PerScreen;
import ru.surfstudio.android.core.ui.base.dagger.CoreActivityScreenModule;
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenComponent;
import ru.surfstudio.standard.app.dagger.ActivityComponent;
import ru.surfstudio.standard.ui.base.configurator.ActivityScreenConfigurator;
import ru.surfstudio.standard.ui.base.dagger.ActivityScreenModule;

/**
 * Конфигуратор активити главного экрана
 */
class MainScreenConfigurator extends ActivityScreenConfigurator {
    @PerScreen
    @Component(dependencies = ActivityComponent.class, modules = {ActivityScreenModule.class})
    interface MainScreenComponent extends ScreenComponent<MainActivityView> {
    }

    MainScreenConfigurator(Activity activity, Intent intent) {
        super(activity, intent);
    }

    @Override
    protected ScreenComponent createScreenComponent(ActivityComponent activityComponent,
                                                    ActivityScreenModule activityScreenModule,
                                                    CoreActivityScreenModule coreActivityScreenModule,
                                                    Intent intent) {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .activityComponent(activityComponent)
                .coreActivityScreenModule(coreActivityScreenModule)
                .activityScreenModule(activityScreenModule)
                .build();
    }

    @Override
    public String getName() {
        return "main";
    }
}
