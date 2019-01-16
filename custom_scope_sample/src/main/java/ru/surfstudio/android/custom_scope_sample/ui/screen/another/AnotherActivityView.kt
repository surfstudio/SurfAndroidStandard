package ru.surfstudio.android.custom_scope_sample.ui.screen.another

import android.os.Bundle
import android.os.PersistableBundle
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_another.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.custom_scope_sample.R
import ru.surfstudio.android.custom_scope_sample.ui.base.LoginScopeStorage
import javax.inject.Inject

class AnotherActivityView : BaseRenderableActivityView<AnotherScreenModel>() {

    @Inject
    internal lateinit var presenter: AnotherPresenter

    override fun createConfigurator() = AnotherScreenConfigurator(intent)

    override fun getContentView(): Int = R.layout.activity_another

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun getScreenName(): String = "AnotherActivityView"

    override fun renderInternal(sm: AnotherScreenModel) {
        another_screen_et.setText(sm.email)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        initListeners()
    }

    private fun initListeners() {
        presenter.observeTextChanges(RxTextView.textChanges(another_screen_et))
    }

    override fun onStart() {
        super.onStart()
        LoginScopeStorage.addActivity(this::class.java)
    }

    override fun onDestroy() {
        LoginScopeStorage.removeActivity(this::class.java)
        super.onDestroy()
    }
}