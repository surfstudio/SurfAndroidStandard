package ru.surfstudio.standard.f_debug.info

import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.activity_info_debug.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.template.f_debug.BuildConfig
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.base_ui.provider.component.ComponentProvider
import javax.inject.Inject

/**
 * Вью экрана показа общей информации
 */
class InfoDebugActivityView : BaseRenderableActivityView<InfoDebugScreenModel>() {

    @Inject
    lateinit var presenter: InfoDebugPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = ComponentProvider.createActivityScreenConfigurator(intent, this::class)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_info_debug

    override fun getScreenName(): String = "debug_info"

    override fun renderInternal(sm: InfoDebugScreenModel) {
        info_package_name_tv.text = getString(R.string.info_package_name_text, packageName)
        info_build_type_tv.text = getString(R.string.info_build_type_text, BuildConfig.BUILD_TYPE)
    }
}
