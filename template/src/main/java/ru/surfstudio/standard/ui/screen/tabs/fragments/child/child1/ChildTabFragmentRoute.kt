package ru.surfstudio.standard.ui.screen.tabs.fragments.child.child1

import android.os.Bundle
import android.support.v4.app.Fragment
import androidx.os.bundleOf
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentWithParamsRoute
import ru.surfstudio.android.logger.Logger


class ChildTabFragmentRoute(val id: Int) : FragmentWithParamsRoute() {

    constructor(bundle: Bundle) : this(bundle.getInt(Route.EXTRA_FIRST))

    override fun prepareBundle(): Bundle = bundleOf(Pair(Route.EXTRA_FIRST,id))

    override fun getFragmentClass(): Class<out Fragment> = ChildTabFragmentView::class.java

    override fun getTag(): String {
        return super.getTag() + id
    }
}