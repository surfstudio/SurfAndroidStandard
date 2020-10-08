package ru.surfstudio.android.navigation.sample_standard.screen.guide

import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class GuideBindModel @Inject constructor() {

    val bottomNavClicked = Action<Unit>()
    val sharedElementClicked = Action<Unit>()
    val dialogsClicked = Action<Unit>()
}