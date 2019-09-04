/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.surfstudio.android.mvp.binding.rx.sample

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

/**
 * Конфигуратор активити главного экрана
 */
class MainScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, MainScreenModule::class])
    internal interface MainScreenComponent
        : BindableScreenComponent<MainActivityView>, SampleDialogComponent

    @Module
    internal class MainScreenModule(route: MainActivityRoute)
        : DefaultCustomScreenModule<MainActivityRoute>(route) {

        @Provides
        @PerScreen
        fun provideSampleDialogBindModel(vb: MainBindModel): SampleDialogBindModel = vb

        @Provides
        @PerScreen
        fun provideCounterBindModel(vb: MainBindModel): CounterBindModel = vb

        @Provides
        @PerScreen
        fun provideMainNavigationBindModel(vb: MainBindModel): MainNavigationBindModel = vb

        @Provides
        @PerScreen
        fun provideDialogControlBindModel(vb: MainBindModel): DialogControlBindModel = vb

        @Provides
        @PerScreen
        fun provideDoubleTextBindModel(vb: MainBindModel): DoubleTextBindModel = vb

        @Provides
        @PerScreen
        fun providePresenters(
                mainPresenter: MainPresenter,
                doubleTextPresenter: DoubleTextPresenter,
                counterPresenter: CounterPresenter,
                mainNavigationPresenter: MainNavigationPresenter,
                dialogControlPresenter: DialogControlPresenter
        ) = Any()
    }

    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .mainScreenModule(MainScreenModule(MainActivityRoute()))
                .build()
    }
}
