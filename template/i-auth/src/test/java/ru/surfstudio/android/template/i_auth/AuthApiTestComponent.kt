package ru.surfstudio.android.template.i_auth

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.template.test_utils.di.components.TestNetworkAppComponent

@PerActivity
@Component(dependencies = [TestNetworkAppComponent::class])
interface AuthApiTestComponent {
    fun inject(test: SampleApiTest)
}