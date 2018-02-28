package ru.surfstudio.standard.ui.screen.tabs.fragments.child.child4

import android.os.Bundle
import android.support.v4.app.Fragment
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentWithParamsRoute
import ru.surfstudio.android.logger.Logger


class Child4TabFragmentRoute(val id: Int) : FragmentWithParamsRoute() {

    constructor(bundle: Bundle) : this(bundle.getInt(Route.EXTRA_FIRST))

    override fun prepareBundle(): Bundle =
            Bundle().apply {
                Logger.d("2222 put int0 bundle $id")
                putInt(Route.EXTRA_FIRST, id)
            }

    override fun getFragmentClass(): Class<out Fragment> = Child4TabFragmentView::class.java
}