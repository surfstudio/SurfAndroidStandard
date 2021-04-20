package ru.surfstudio.android.navigation.sample_standard.screen.search.results

import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindModel
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class SearchResultBindModel @Inject constructor(
        route: SearchResultRoute
) : BindModel {
    val textState = State<String>(route.searchInput)
    val backClick = Action<Unit>()
}
