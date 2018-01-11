package ru.surfstudio.standard.ui.common.contentcontainer

import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import ru.surfstudio.android.core.ui.base.navigation.Route

import java.io.Serializable

import ru.surfstudio.android.core.ui.base.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.core.ui.base.navigation.fragment.route.FragmentWithParamsRoute

/**
 * Маршрут рутового экрана для дочерних фрагментов
 */
class ContentContainerFragmentRoute : FragmentWithParamsRoute {
    val initialRoute: FragmentRoute

    /**
     * initialRoute должен быть Serializable
     */
    constructor( initialRoute: FragmentRoute) {
        this.initialRoute = initialRoute
    }

    internal constructor(args: Bundle) {
        initialRoute = args.getSerializable(Route.EXTRA_FIRST) as FragmentRoute
    }

    override fun getTag(): String {
        return super.getTag() + ":" + initialRoute.tag
    }

    override fun prepareBundle(): Bundle {
        val args = Bundle(1)
        args.putSerializable(Route.EXTRA_FIRST, initialRoute as Serializable)
        args.putString(ContentContainerScreenConfigurator.EXTRA_INITIAL_ROUTE_KEY, initialRoute.tag)
        return args
    }

    override fun getFragmentClass(): Class<out Fragment> {
        return ContentContainerFragmentView::class.java
    }
}
