package ru.surfstudio.android.custom_scope_sample.ui.screen.another

import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.custom_scope_sample.R
import ru.surfstudio.android.custom_scope_sample.ui.base.LoginScopeStorage
import javax.inject.Inject

class AnotherActivityView : BaseRenderableActivityView<AnotherScreenModel>() {

    @Inject
    internal lateinit var presenter: AnotherPresenter

    override fun createConfigurator() = AnotherScreenConfigurator(this, intent)

    override fun getContentView(): Int = R.layout.activity_another

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun getScreenName(): String = "AnotherActivityView"

    override fun renderInternal(screenModel: AnotherScreenModel?) {}

    override fun onStart() {
        super.onStart()
        LoginScopeStorage.activityList.add(this::class.java)
    }

    override fun onDestroy() {
        LoginScopeStorage.removeActivity(this::class.java)
        super.onDestroy()
    }
}