package ru.surfstudio.android.filestorage.sample.ui.base.dagger.activity

import dagger.Component
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.filestorage.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.filestorage.sample.interactor.ip.IpRepository
import ru.surfstudio.android.filestorage.sample.interactor.ip.cache.IpJsonStorageWrapper
import ru.surfstudio.android.filestorage.sample.interactor.ip.cache.IpSerializableStorageWrapper
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule

/**
 * Компонент для @PerActivity скоупа
 */

@PerActivity
@Component(
        dependencies = [(CustomAppComponent::class)],
        modules = [(DefaultActivityModule::class)])
interface CustomActivityComponent {
    fun schedulerProvider(): SchedulersProvider
    fun connectionProvider(): ConnectionProvider
    fun activityProvider(): ActivityProvider
    fun stringsProvider(): StringsProvider

    fun activityPersistentScope(): ActivityPersistentScope

    fun ipJsonStorageWrapper(): IpJsonStorageWrapper
    fun ipSerializableStorageWrapper(): IpSerializableStorageWrapper
    fun ipRepository(): IpRepository
}