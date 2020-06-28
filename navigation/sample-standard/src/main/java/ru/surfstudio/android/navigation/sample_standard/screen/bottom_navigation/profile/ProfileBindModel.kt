package ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation.profile

import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindModel
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class ProfileBindModel @Inject constructor() : BindModel {

    val openSettings = Action<Unit>()
    val openConfirmLogoutScreen = Action<Unit>()
}