package ru.surfstudio.standard.i_auth.di

import dagger.Component
import ru.surfstudio.standard.i_auth.SampleApiTest
import ru.surfstudio.standard.test_utils.di.components.TestNetworkAppComponent
import ru.surfstudio.standard.test_utils.di.scope.PerTest

@PerTest
@Component(dependencies = [TestNetworkAppComponent::class], modules = [TestAuthApiModule::class])
interface TestAuthApiComponent {

    fun inject(test: SampleApiTest)
}