package ru.surfstudio.android.navigation.sample_standard.screen.search.request

import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindModel
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class SearchRequestBindModel @Inject constructor(
        route: SearchRequestRoute
) : BindModel {
    val resultClick = Action<Unit>()
    val textChanges = Action<String>()
    val textState = State<String>(route.searchInput)
    val closeClick = Action<Unit>()
}
