package ru.surfstudio.standard.app.dagger

import com.example.camera.PhotoProvider
import dagger.Component
import ru.surfstudio.android.core.ui.base.dagger.BaseCoreActivityComponent
import ru.surfstudio.android.core.ui.base.dagger.CoreActivityModule
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.standard.app.intialization.InitializeAppInteractor

/**
 * Created by makstuev on 30.01.2018.
 */

@PerActivity
@Component(dependencies = [(AppComponent::class)],
        modules = [(CoreActivityModule::class)])
interface ActivityComponent : BaseCoreActivityComponent {
    fun initializeAppInteractor(): InitializeAppInteractor
    fun photoPresenter(): PhotoProvider
}