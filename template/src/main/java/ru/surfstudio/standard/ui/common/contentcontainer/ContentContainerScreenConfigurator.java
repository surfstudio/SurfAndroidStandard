package ru.surfstudio.standard.ui.common.contentcontainer;


import android.app.Activity;
import android.os.Bundle;

import dagger.Component;
import dagger.Module;
import ru.surfstudio.android.core.app.dagger.scope.PerScreen;
import ru.surfstudio.android.core.ui.base.dagger.CoreFragmentScreenModule;
import ru.surfstudio.android.core.ui.base.dagger.CustomScreenModule;
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenComponent;
import ru.surfstudio.standard.app.dagger.ActivityComponent;
import ru.surfstudio.standard.ui.base.configurator.FragmentScreenConfigurator;
import ru.surfstudio.standard.ui.base.dagger.FragmentScreenModule;

/**
 * Конфигуратор рутового экрана для дочерних фрагментов
 */
class ContentContainerScreenConfigurator extends FragmentScreenConfigurator {
    static final String EXTRA_INITIAL_ROUTE_KEY = "extra:initial_route_key";

    private final String initialRouteKey;

    @PerScreen
    @Component(dependencies = ActivityComponent.class, modules = {FragmentScreenModule.class, ContentContainerScreenModule.class})
    interface ContentContainerScreenComponent extends ScreenComponent<ContentContainerFragmentView> {
    }

    @Module
    static class ContentContainerScreenModule extends CustomScreenModule<ContentContainerFragmentRoute> {
        ContentContainerScreenModule(ContentContainerFragmentRoute route) {
            super(route);
        }
    }

    ContentContainerScreenConfigurator(Activity activity, Bundle args) {
        super(activity, args);
        initialRouteKey = args != null ? args.getString(EXTRA_INITIAL_ROUTE_KEY, null) : null;

        if (initialRouteKey == null || initialRouteKey.trim().length() == 0) {
            throw new IllegalStateException("Ключ для initialRouteKey не установлен");
        }
    }

    @Override
    protected ScreenComponent createScreenComponent(ActivityComponent parentComponent,
                                                    FragmentScreenModule fragmentScreenModule,
                                                    CoreFragmentScreenModule corefragmentScreenModule,
                                                    Bundle args) {
        return DaggerContentContainerScreenConfigurator_ContentContainerScreenComponent.builder()
                .activityComponent(parentComponent)
                .fragmentScreenModule(fragmentScreenModule)
                .coreFragmentScreenModule(corefragmentScreenModule)
                .contentContainerScreenModule(new ContentContainerScreenModule(new ContentContainerFragmentRoute(args)))
                .build();
    }

    @Override
    public String getName() {
        return "content_container:" + initialRouteKey;
    }
}
