package ru.surfstudio.android.shared.pref.sample.ui.base.dagger.activity

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule
import ru.surfstudio.android.shared.pref.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.shared.pref.sample.interactor.ip.IpRepository
import ru.surfstudio.android.shared.pref.sample.interactor.ip.cache.IpStorage

/**
 * Компонент для @PerActivity скоупа
 */

@PerActivity
@Component(dependencies = [(CustomAppComponent::class)],
        modules = [(DefaultActivityModule::class)])
interface CustomActivityComponent : DefaultActivityComponent {
    fun ipStorage(): IpStorage
    fun ipRepository(): IpRepository
}