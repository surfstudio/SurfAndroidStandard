package ru.surfstudio.android.filestorage.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.activity.BaseLdsActivityView
import ru.surfstudio.android.core.mvp.placeholder.PlaceHolderViewInterface
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.filestorage.sample.R
import ru.surfstudio.android.filestorage.sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.message.MessageController
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseLdsActivityView<MainScreenModel>() {

    override fun getScreenName(): String = "MainActivity"

    @Inject
    internal lateinit var presenter: MainPresenter

    @Inject
    internal lateinit var messageController: MessageController

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): ActivityScreenConfigurator = MainScreenConfigurator(intent)

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun getPlaceHolderView(): PlaceHolderViewInterface = placeholder

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initListeners()
    }

    override fun renderInternal(screenModel: MainScreenModel) {
        placeholder.render(screenModel.loadState)
    }

    private fun initListeners() {
        placeholder.buttonLambda = { presenter.reloadData() }
    }
}
