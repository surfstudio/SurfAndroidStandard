package ru.surfstudio.android.core.mvi.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvi.event.hub.owner.SingleHubOwner
import ru.surfstudio.android.core.mvi.sample.R
import ru.surfstudio.android.core.mvi.sample.ui.base.hub.BaseEventHub
import ru.surfstudio.android.core.mvi.ui.BaseReactActivityView
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.logger.logging_strategies.impl.timber.TimberLoggingStrategy
import javax.inject.Inject

/**
 * Главный экран с навигацией по остальным
 * Демонстрирует механизм  Navigation Middleware, скрывающий детали навигации в middleware и эвентах
 */
class MainActivityView : BaseReactActivityView(), SingleHubOwner<MainEvent> {

    @Inject
    override lateinit var hub: BaseEventHub<MainEvent>

    override fun createConfigurator() = MainScreenConfigurator(intent)

    override fun getScreenName(): String = "MainActivityView"

    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        initLog()

        //самая обычная запись эмита события, явное указание, без экстеншнов
        main_open_input_form_btn.clicks() bindTo { hub.emit(MainEvent.OpenInputForm()) }

        //облегченная запись с помощью экстеншна в SingleHubOwner
        main_open_simple_list_btn.clicks() bindTo { MainEvent.OpenSimpleList().emit() }

        //еще более простая запись при помощи экстеншна в SingleHubOwner
        main_open_complex_list_btn.clicks().emit(MainEvent.OpenComplexList())
    }

    private fun initLog() {
        Logger.addLoggingStrategy(TimberLoggingStrategy())
    }
}