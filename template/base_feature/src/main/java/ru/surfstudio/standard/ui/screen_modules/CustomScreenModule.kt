package ru.surfstudio.standard.ui.screen_modules

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.route.Route

/**
 * базовый класс для дополнительного модуля конкретного экрана (обычно используется для поставки
 * параметров, переданных на экран при старте в презентер, в этом случае параметры передаются, обернутые в Route)
 *
 * @param <R>
</R> */
@Module
abstract class CustomScreenModule<out R : Route>(private val route: R) {

    @Provides
    @PerScreen
    fun provideRoute(): R {
        return route
    }
}