package ru.surfstudio.android.navigation.sample_standard.screen.search.request

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.observer.route.ResultRoute
import ru.surfstudio.android.navigation.route.Route
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

/**
 * Роут экрана на котором осуществляется поиск
 *
 * @param searchInput строка с поисковым запросом, предзаполненная на старте экрана
 */
class SearchRequestRoute(val searchInput: String = EMPTY_STRING): FragmentRoute(), ResultRoute<String> {

    constructor(bundle: Bundle): this(bundle.getString(Route.EXTRA_FIRST, EMPTY_STRING))

    override fun prepareData(): Bundle {
        return bundleOf(Route.EXTRA_FIRST to searchInput)
    }

    override fun getScreenClass(): Class<out Fragment> {
        return SearchRequestFragmentView::class.java
    }
}