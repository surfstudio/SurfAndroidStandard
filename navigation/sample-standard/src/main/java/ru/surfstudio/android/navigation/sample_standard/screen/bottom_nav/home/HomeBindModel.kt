package ru.surfstudio.android.navigation.sample_standard.screen.bottom_nav.home

import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindModel
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class HomeBindModel @Inject constructor() : BindModel {
    val openNestedScreenAction = Action<Unit>()
}