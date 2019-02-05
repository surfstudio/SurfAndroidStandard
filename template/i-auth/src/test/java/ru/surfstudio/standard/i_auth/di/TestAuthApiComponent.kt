package ru.surfstudio.standard.i_auth.di

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.standard.i_auth.SampleApiTest
import ru.surfstudio.standard.test_utils.di.components.TestNetworkAppComponent

@PerActivity
@Component(dependencies = [TestNetworkAppComponent::class], modules = [TestAuthApiModule::class])
interface TestAuthApiComponent {

    fun inject(test: SampleApiTest)
}