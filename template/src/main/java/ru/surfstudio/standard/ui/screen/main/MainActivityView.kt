package ru.surfstudio.standard.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import android.widget.Button
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.standard.R
import ru.surfstudio.standard.ui.base.configurator.ActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {


    @Inject
    internal lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        val btn: Button = findViewById(R.id.activity_main_btn)
        btn.setOnClickListener { presenter.OnBtnClick() }
    }

    @IdRes
    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun renderInternal(screenModel: MainScreenModel) {}

    override fun getName(): String {
        return "main"
    }

    override fun getPresenters(): Array<CorePresenter<*>> {
        return arrayOf(presenter)
    }

    override fun createConfigurator(): ActivityScreenConfigurator {
        return MainScreenConfigurator(intent)
    }


}
