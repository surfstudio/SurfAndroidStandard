package ru.surfstudio.standard.app.dagger

import dagger.Component
import ru.surfstudio.android.core.app.dagger.scope.PerActivity
import ru.surfstudio.android.core.ui.base.dagger.BaseCoreActivityComponent
import ru.surfstudio.android.core.ui.base.dagger.CoreActivityModule
import ru.surfstudio.standard.app.intialization.InitializeAppInteractor

/**
 * Created by makstuev on 30.01.2018.
 */

@PerActivity
@Component(dependencies = [(AppComponent::class)],
        modules = [(CoreActivityModule::class)])
interface ActivityComponent : BaseCoreActivityComponent {
    fun initializeAppInteractor(): InitializeAppInteractor
}