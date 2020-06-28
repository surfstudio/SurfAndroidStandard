package ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation

import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class BottomNavBindModel @Inject constructor() {

    val bottomNavClicked = Action<BottomNavTabType>()
    val backPressed = Action<Unit>()
}