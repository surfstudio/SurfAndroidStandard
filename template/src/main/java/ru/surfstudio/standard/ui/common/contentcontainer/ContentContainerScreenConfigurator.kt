package ru.surfstudio.standard.ui.common.contentcontainer


import android.app.Activity
import android.os.Bundle

import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.ui.base.dagger.CoreFragmentScreenModule
import ru.surfstudio.android.core.ui.base.dagger.CustomScreenModule
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenComponent
import ru.surfstudio.standard.app.dagger.ActivityComponent
import ru.surfstudio.standard.ui.base.configurator.FragmentScreenConfigurator
import ru.surfstudio.standard.ui.base.dagger.FragmentScreenModule

/**
 * Конфигуратор рутового экрана для дочерних фрагментов
 */
internal class ContentContainerScreenConfigurator(activity: Activity, args: Bundle) : FragmentScreenConfigurator(activity, args) {

    private val initialRouteKey: String?

    @PerScreen
    @Component(dependencies = arrayOf(ActivityComponent::class), modules = arrayOf(FragmentScreenModule::class, ContentContainerScreenModule::class))
    internal interface ContentContainerScreenComponent : ScreenComponent<ContentContainerFragmentView>

    @Module
    internal class ContentContainerScreenModule(route: ContentContainerFragmentRoute) : CustomScreenModule<ContentContainerFragmentRoute>(route)

    init {
        initialRouteKey = args.getString(EXTRA_INITIAL_ROUTE_KEY, null)

        if (initialRouteKey == null || initialRouteKey.trim { it <= ' ' }.length == 0) {
            throw IllegalStateException("Ключ для initialRouteKey не установлен")
        }
    }

    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       fragmentScreenModule: FragmentScreenModule,
                                       corefragmentScreenModule: CoreFragmentScreenModule,
                                       args: Bundle): ScreenComponent<*> {
        return DaggerContentContainerScreenConfigurator_ContentContainerScreenComponent.builder()
                .activityComponent(parentComponent)
                .fragmentScreenModule(fragmentScreenModule)
                .coreFragmentScreenModule(corefragmentScreenModule)
                .contentContainerScreenModule(ContentContainerScreenModule(ContentContainerFragmentRoute(args)))
                .build()
    }

    override fun getName(): String {
        return "content_container:" + initialRouteKey!!
    }

    companion object {
        val EXTRA_INITIAL_ROUTE_KEY = "extra:initial_route_key"
    }
}
