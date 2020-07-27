package ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.main

import android.os.Bundle
import android.os.PersistableBundle
import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvi.event.hub.owner.SingleHubOwner
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.open
import ru.surfstudio.android.core.mvi.sample.R
import ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.input.InputFormActivityRoute
import ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.kitties.KittiesActivityRoute
import ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.list.ComplexListActivityRoute
import ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.main.MainEvent.Navigation
import ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.simple_list.SimpleListActivityRoute
import ru.surfstudio.android.core.mvi.ui.BaseReactActivityView
import javax.inject.Inject

/**
 * Главный экран с навигацией по остальным
 * Демонстрирует механизм  Navigation Middleware, скрывающий детали навигации в middleware и эвентах
 */
class MainActivityView : BaseReactActivityView(), SingleHubOwner<MainEvent> {

    @Inject
    override lateinit var hub: ScreenEventHub<MainEvent>

    override fun createConfigurator() = MainScreenConfigurator(intent)

    override fun getScreenName(): String = "MainActivityView"

    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        //самая обычная запись эмита события, явное указание, без экстеншнов
        main_open_input_form_btn.clicks() bindTo { hub.emit(Navigation().open(InputFormActivityRoute())) }

        //облегченная запись с помощью экстеншна в SingleHubOwner
        main_open_simple_list_btn.clicks() bindTo { Navigation().open(SimpleListActivityRoute()).emit() }

        //еще более простая запись при помощи экстеншна в SingleHubOwner
        main_open_complex_list_btn.clicks().emit(Navigation().open(ComplexListActivityRoute()))
        main_open_mvi_reducer_screen_btn.clicks().emit(Navigation().open(KittiesActivityRoute()))
    }
}