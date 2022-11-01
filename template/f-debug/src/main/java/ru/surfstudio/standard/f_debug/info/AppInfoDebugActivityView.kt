package ru.surfstudio.standard.f_debug.info

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.android.template.f_debug.BuildConfig
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.android.template.f_debug.databinding.ActivityAppInfoDebugBinding
import ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity.AppInfoDebugScreenConfigurator
import javax.inject.Inject

/**
 * Вью экрана показа общей информации
 */
class AppInfoDebugActivityView : BaseRenderableActivityView<AppInfoDebugScreenModel>() {

    private val binding by viewBinding(ActivityAppInfoDebugBinding::bind) { rootView }

    @Inject
    lateinit var presenter: AppInfoDebugPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = AppInfoDebugScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_app_info_debug

    override fun getScreenName(): String = "debug_info"

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        initContent()
    }

    override fun renderInternal(sm: AppInfoDebugScreenModel) {}

    @SuppressLint("StringFormatMatches")
    private fun initContent() {
        with(packageManager.getPackageInfo(packageName, 0)) {
            binding.debugAppInfoVersionCodeTv.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                 getString(R.string.debug_app_info_version_code_text, longVersionCode)
            } else {
                getString(R.string.debug_app_info_version_code_text, versionCode)
            }
            binding.debugAppInfoVersionNameTv.text = getString(R.string.debug_app_info_version_name_text, versionName)
        }
        binding.debugAppInfoPackageNameTv.text = getString(R.string.debug_app_info_package_name_text, packageName)
        binding.debugAppInfoBuildTypeTv.text = getString(R.string.debug_app_info_build_type_text, BuildConfig.BUILD_TYPE)
    }
}
