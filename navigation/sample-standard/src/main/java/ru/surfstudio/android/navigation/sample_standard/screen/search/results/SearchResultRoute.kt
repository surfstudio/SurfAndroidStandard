package ru.surfstudio.android.navigation.sample_standard.screen.search.results

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.Route
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

class SearchResultRoute(val searchInput: String): FragmentRoute() {

    constructor(bundle: Bundle): this(bundle.getString(Route.EXTRA_FIRST, EMPTY_STRING))

    override fun getId(): String {
        return super.getId() + searchInput
    }

    override fun prepareData(): Bundle {
        return bundleOf(Route.EXTRA_FIRST to searchInput)
    }

    override fun getScreenClass(): Class<out Fragment> {
        return SearchResultFragmentView::class.java
    }

}
