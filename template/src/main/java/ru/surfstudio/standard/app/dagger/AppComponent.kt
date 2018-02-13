package ru.surfstudio.standard.app.dagger

import android.content.Context
import dagger.Component
import ru.surfstudio.android.core.app.connection.ConnectionProvider
import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import ru.surfstudio.android.core.app.scheduler.SchedulersProvider
import ru.surfstudio.android.core.util.ActiveActivityHolder
import ru.surfstudio.standard.app.intialization.InitializeAppInteractor
import ru.surfstudio.standard.app.intialization.migration.MigrationModule
import ru.surfstudio.standard.interactor.auth.AuthModule
import ru.surfstudio.standard.interactor.auth.SessionChangedInteractor
import ru.surfstudio.standard.interactor.common.network.NetworkModule
import ru.surfstudio.standard.interactor.common.network.OkHttpModule

@PerApplication
@Component(modules = [
    (AppModule::class),
    (MigrationModule::class),
    (ActiveActivityHolderModule::class),
    (AuthModule::class),
    (NetworkModule::class),
    (OkHttpModule::class)])
interface AppComponent {
    fun initializeAppInteractor(): InitializeAppInteractor
    fun context(): Context
    fun schedulerProvider(): SchedulersProvider
    fun activeActivityHolder(): ActiveActivityHolder
    fun connectionProvider(): ConnectionProvider
    fun sessionChangeInteractor(): SessionChangedInteractor
    fun photoPresenter(): PhotoProvider
}
