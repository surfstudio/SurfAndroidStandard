package ru.surfstudio.standard.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.ui.base.screen.activity.BaseRenderableHandleableErrorActivityView
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter
import ru.surfstudio.android.imageloader.ImageLoader
import ru.surfstudio.standard.R
import ru.surfstudio.standard.R.id.image_view
import ru.surfstudio.standard.ui.base.configurator.ActivityScreenConfigurator

import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableHandleableErrorActivityView<MainScreenModel>() {

    @Inject
    internal lateinit var presenter: MainPresenter

    override fun getPresenters(): Array<CorePresenter<*>> {
        return arrayOf(presenter)
    }

    @IdRes
    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun createConfigurator(): ActivityScreenConfigurator {
        return MainScreenConfigurator(this, intent)
    }

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        ImageLoader
                .with(this)
                .url("url")
                .into(image_view)
    }

    override fun renderInternal(screenModel: MainScreenModel) {}
}