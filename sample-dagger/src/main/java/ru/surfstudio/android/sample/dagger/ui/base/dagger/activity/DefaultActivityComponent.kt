package ru.surfstudio.android.sample.dagger.ui.base.dagger.activity

import android.content.Context
import dagger.Component
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.ui.bus.RxBus
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.picturechooser.PicturePermissionChecker
import ru.surfstudio.android.picturechooser.PictureProvider
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppComponent
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Компонент для @PerActivity скоупа
 */

@PerActivity
@Component(dependencies = [(DefaultAppComponent::class)],
        modules = [(DefaultActivityModule::class)])
interface DefaultActivityComponent {
    fun schedulerProvider(): SchedulersProvider
    fun connectionProvider(): ConnectionProvider
    fun stringsProvider(): StringsProvider

    fun activityProvider(): ActivityProvider
    fun activityPersistentScope(): ActivityPersistentScope
    fun context(): Context
    fun picturePermissionChecker(): PicturePermissionChecker
    fun providePictureProvider(): PictureProvider
    fun rxBus(): RxBus
}