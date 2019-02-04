package ru.surfstudio.android.sample.dagger.ui.base.dagger.screen;


import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.ui.navigation.Route;
import ru.surfstudio.android.dagger.scope.PerScreen;

/**
 * базовый класс для дополнительного модуля конкретного экрана (обычно используется для поставки
 * параметров, переданных на экран при старте в презентер, в этом случае параметры передаются, обернутые в Route)
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
