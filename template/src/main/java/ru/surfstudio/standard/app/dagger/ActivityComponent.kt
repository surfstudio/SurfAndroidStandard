package ru.surfstudio.standard.app.dagger

import dagger.Component
import ru.surfstudio.android.core.app.dagger.scope.PerActivity
import ru.surfstudio.android.core.ui.base.dagger.BaseCoreActivityComponent
import ru.surfstudio.standard.app.intialization.InitializeAppInteractor

@PerActivity
@Component(dependencies = [(AppComponent::class)])
interface ActivityComponent : BaseCoreActivityComponent {
    fun initializeAppInteractor(): InitializeAppInteractor
}
