package ru.surfstudio.android.easyadapter.sample.ui.base.dagger.activity

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.easyadapter.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.easyadapter.sample.interactor.FirstDataRepository
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule

@PerActivity
@Component(dependencies = [(CustomAppComponent::class)],
        modules = [(DefaultActivityModule::class)])
interface CustomActivityComponent : DefaultActivityComponent {
    fun firstDataRepository(): FirstDataRepository
}