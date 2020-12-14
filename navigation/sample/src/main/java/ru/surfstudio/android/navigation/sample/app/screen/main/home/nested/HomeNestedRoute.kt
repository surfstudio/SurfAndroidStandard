package ru.surfstudio.android.navigation.sample.app.screen.main.home.nested

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.Route
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

class HomeNestedRoute(
        val order: Int = 1
) : FragmentRoute() {

    constructor(bundle: Bundle?) : this(bundle?.getInt(Route.EXTRA_FIRST) ?: 0)

    override fun getScreenClass(): Class<out Fragment>? = HomeNestedFragment::class.java

    override fun prepareData(): Bundle = bundleOf(Route.EXTRA_FIRST to order)

    override fun getId(): String = super.getId() + order
}