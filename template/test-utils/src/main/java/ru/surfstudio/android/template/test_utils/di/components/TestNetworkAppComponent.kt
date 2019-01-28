package ru.surfstudio.android.template.test_utils.di.components

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.template.test_utils.di.modules.TestAppModule
import ru.surfstudio.android.template.test_utils.di.modules.TestNetworkModule
import ru.surfstudio.android.template.test_utils.di.modules.TestOkHttpModule
import ru.surfstudio.android.template.test_utils.di.modules.CacheModule
import ru.surfstudio.android.template.test_utils.BaseNetworkDaggerTest

@PerApplication
@Component(modules = [
    TestAppModule::class,
    TestNetworkModule::class,
    TestOkHttpModule::class,
    CacheModule::class])
interface TestNetworkAppComponent {

    fun inject(test: BaseNetworkDaggerTest<TestNetworkAppComponent>)
}