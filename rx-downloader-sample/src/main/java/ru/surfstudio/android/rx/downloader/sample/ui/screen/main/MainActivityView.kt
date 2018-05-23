package ru.surfstudio.android.rx.downloader.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.LayoutRes
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.rx.downloader.sample.R
import ru.surfstudio.android.rx.downloader.sample.ui.base.configurator.ActivityScreenConfigurator
import javax.inject.Inject

class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {

    @Inject
    lateinit var presenter: MainPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)


    override fun createConfigurator(): ActivityScreenConfigurator = MainScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        findViews()
        initListeners()
    }

    override fun renderInternal(screenModel: MainScreenModel?) {

    }

    private fun findViews() {
    }

    private fun initListeners() {
        main_activity_download_btn.setOnClickListener { presenter.onDownloadBtnClick() }
    }

    override fun getScreenName(): String = "main"
}
