package ru.surfstudio.android.sample.dagger.ui.base.dagger.screen;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.ui.navigation.Route;
import ru.surfstudio.android.dagger.scope.PerScreen;

/**
 * Base class for additional screen module.
 * Usually is used to provide params which are passed to screen in its presenter on start,
 * in such case params are passes using [Route]
 *
 * @param <R>
 */
@Module
public abstract class DefaultCustomScreenModule<R extends Route> {

    private R route;

    public DefaultCustomScreenModule(R route) {
        this.route = route;
    }

    @Provides
    @PerScreen
    R provideRoute() {
        return route;
    }
}
