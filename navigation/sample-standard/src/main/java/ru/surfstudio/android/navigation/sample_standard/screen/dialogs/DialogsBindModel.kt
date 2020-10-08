package ru.surfstudio.android.navigation.sample_standard.screen.dialogs

import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class DialogsBindModel @Inject constructor() {
    val openDialogButtonClicked = Action<Unit>()
    val openDialogWithFadeButtonClicked = Action<Unit>()
    val openDialogWithSlideButtonClicked = Action<Unit>()
}