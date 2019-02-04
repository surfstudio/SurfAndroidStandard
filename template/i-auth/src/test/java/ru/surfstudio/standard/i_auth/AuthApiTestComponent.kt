package ru.surfstudio.standard.i_auth

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.standard.test_utils.di.components.TestNetworkAppComponent

@PerActivity
@Component(dependencies = [TestNetworkAppComponent::class])
interface AuthApiTestComponent {
    fun inject(test: SampleApiTest)
}