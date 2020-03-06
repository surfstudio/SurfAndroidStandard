package ru.surfstudio.android.mvp.binding.rx.sample.twoway

import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindModel
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Модель [TwoWayActivityView].
 */
@PerScreen
class TwoWayBindModel @Inject constructor() : BindModel {

    val moneyInputAction = Action<String>()
    val moneyState = State<String>()
}