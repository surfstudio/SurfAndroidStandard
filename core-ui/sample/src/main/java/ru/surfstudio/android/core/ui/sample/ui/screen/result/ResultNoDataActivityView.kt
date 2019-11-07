package ru.surfstudio.android.core.ui.sample.ui.screen.result

import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.ui.sample.R
import javax.inject.Inject

/**
 * Тестовая активити для возврата результата без данных
 */
class ResultNoDataActivityView : BaseRenderableActivityView<ResultScreenModel>() {

    @Inject
    internal lateinit var presenter: ResultNoDataPresenter

    override fun createConfigurator(): BaseActivityViewConfigurator<*, *, *> =
            ResultScreenConfigurator(intent)

    override fun getScreenName() = "ResultNoDataActivityView"

    override fun getContentView() = R.layout.activity_main

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun renderInternal(sm: ResultScreenModel) {
        // do noting
    }
}