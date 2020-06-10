package ru.surfstudio.android.sample.dagger.ui.base.dagger.screen;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.ui.navigation.Route;
import ru.surfstudio.android.dagger.scope.PerScreen;

/**
 * Base class for additional screen module.
 * Used to provide initial params to a presenter on screen start.
 * Such params are wrapped in [Route].
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
    public R provideRoute() {
        return route;
    }
}
