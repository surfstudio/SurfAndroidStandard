package ru.surfstudio.standard.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import com.jakewharton.rxbinding2.widget.RxTextView
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import ru.surfstudio.android.core.ui.base.screen.activity.BaseRenderableHandleableErrorActivityView
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter
import ru.surfstudio.android.core.ui.base.screen.widjet.TitleSubtitleView
import ru.surfstudio.standard.R
import ru.surfstudio.standard.ui.base.configurator.ActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableHandleableErrorActivityView<MainScreenModel>() {

    @Inject internal lateinit var presenter: MainPresenter

    val titleView by lazy { findViewById<TitleSubtitleView>(R.id.title_subtitle_view) }

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
    }

    override fun renderInternal(screenModel: MainScreenModel) {
        titleView.onTitleClickListenerCallback = { toast(it) }
        titleView.onSubTitleClickListenerCallback = { toast(it)}

        RxTextView.textChanges(find(R.id.et)).subscribe { titleView.titleText = it.toString() }
    }
}
