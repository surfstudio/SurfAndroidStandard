package ru.surfstudio.android.mvpwidget.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.IdRes
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.ui.FragmentContainer
import ru.surfstudio.android.mvpwidget.sample.R
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>(), FragmentContainer {

    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun renderInternal(sm: MainScreenModel) {}

    override fun getScreenName(): String = "main"

    override fun getContentContainerViewId(): Int = R.id.content

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): DefaultActivityScreenConfigurator = MainScreenConfigurator(intent)

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        list_btn.setOnClickListener { presenter.openListScreen() }
        fragment_btn.setOnClickListener { presenter.openWidgetFragment() }
    }
}
