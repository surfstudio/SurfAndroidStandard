package ru.surfstudio.android.navigation.sample_standard.di.ui.screen

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.route.Route

@Module
abstract class RouteScreenModule<R : Route>(val route: R) : ScreenModule() {

    @Provides
    @PerScreen
    fun provideRoute(): R = route
}