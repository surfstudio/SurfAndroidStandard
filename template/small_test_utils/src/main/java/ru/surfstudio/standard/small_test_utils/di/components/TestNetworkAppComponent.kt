package ru.surfstudio.standard.small_test_utils.di.components

import dagger.Component
import retrofit2.Retrofit
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.small_test_utils.BaseNetworkDaggerTest
import ru.surfstudio.standard.small_test_utils.di.modules.*

@PerApplication
@Component(modules = [TestAppModule::class,
    TestNetworkModule::class,
    TestOkHttpModule::class,
    CacheModule::class,
    TestSharedPrefModule::class,
    EtagModule::class])
interface TestNetworkAppComponent {

    fun inject(test: BaseNetworkDaggerTest<TestNetworkAppComponent>)

    fun provideRetrofit(): Retrofit
}