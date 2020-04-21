package ru.surfstudio.standard.f_main

import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindModel
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.ui.navigation.MainTabType
import javax.inject.Inject

/**
 * Модель главного экрана
 */
@PerScreen
class MainBindModel @Inject constructor() : BindModel {
    val tabTypeState: State<MainTabType> = State(MainTabType.FEED)
    val tabSelectedAction: Action<MainTabType> = Action()
}
