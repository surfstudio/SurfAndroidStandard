package ru.surfstudio.standard.f_main

import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Bond
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindModel
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.ui.navigation.MainTabType
import javax.inject.Inject

/**
 * Модель главного экрана
 */
@PerScreen
class MainBindModel @Inject constructor() : BindModel {
    val tabType: Bond<MainTabType> = Bond(MainTabType.FEED)
}
