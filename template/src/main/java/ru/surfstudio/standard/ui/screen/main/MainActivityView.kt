package ru.surfstudio.standard.ui.screen.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import android.support.v7.widget.LinearSnapHelper
import ru.surfstudio.android.core.ui.base.screen.activity.BaseRenderableHandleableErrorActivityView
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenConfigurator
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter
import ru.surfstudio.standard.R
import ru.surfstudio.standard.ui.base.configurator.ActivityConfigurator
import ru.surfstudio.standard.ui.common.widget.carousel.CarouselView
import ru.surfstudio.standard.ui.common.widget.carousel.snapHelpers.StartSnapHelper
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

    override fun createActivityConfigurator(): BaseActivityConfigurator<*, *> {
        return ActivityConfigurator(this)
    }

    override fun createScreenConfigurator(activity: Activity, intent: Intent): ScreenConfigurator<*> {
        return MainScreenConfigurator(activity, intent)
    }


    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initViews()
    }

    override fun renderInternal(screenModel: MainScreenModel) {}

    fun initViews() {
        val carousel: CarouselView<TestItem> = findViewById(R.id.activity_main_cv)
        val itemList = mutableListOf(TestItem("1"),
                TestItem("2"),
                TestItem("3"),
                TestItem("4"),
                TestItem("5"),
                TestItem("6"))

        val controller = TestController()

        val snapHelper: LinearSnapHelper = StartSnapHelper()
        snapHelper.attachToRecyclerView(carousel)

        carousel.render(itemList, controller)
    }
}
