package ru.surfstudio.android.core.mvi.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvi.sample.R
import ru.surfstudio.android.core.mvi.sample.ui.base.hub.BaseEventHub
import ru.surfstudio.android.core.mvi.ui.BaseReactActivityView
import javax.inject.Inject

class MainActivityView : BaseReactActivityView() {

    @Inject
    lateinit var hub: BaseEventHub<MainEvent>

    override fun createConfigurator() = MainScreenConfigurator(intent)

    override fun getScreenName(): String = "MainActivityView"

    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        main_open_complex_list_btn.clicks() bindTo { hub.emit(MainEvent.OpenComplexList) }
    }
}