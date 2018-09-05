package ru.surfstudio.android.custom_scope_sample.ui.base.dagger.screen;


import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.ui.navigation.Route;
import ru.surfstudio.android.dagger.scope.PerScreen;

/**
 * базовый класс для дополнительного модуля конкретного экрана (обычно используется для поставки
 * параметров, переданных на экран при старте в презентер, в этом случае параметры передаются обурнутые в Route)
 *
 * @param <R>
 */
@Module
public abstract class CustomScreenModule<R extends Route> {

    private R route;

    public CustomScreenModule(R route) {
        this.route = route;
    }

    @Provides
    @PerScreen
    public R provideRoute() {
        return route;
    }
}
