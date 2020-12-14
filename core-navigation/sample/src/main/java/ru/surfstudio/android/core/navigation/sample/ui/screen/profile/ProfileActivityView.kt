package ru.surfstudio.android.core.navigation.sample.ui.screen.profile

import androidx.annotation.IdRes
import kotlinx.android.synthetic.main.activity_profile.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.navigation.sample.R
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью экрана профиля
 */
class ProfileActivityView : BaseRenderableActivityView<ProfileScreenModel>() {
    override fun getScreenName(): String = "ProfileActivity"

    @Inject
    internal lateinit var presenter: ProfilePresenter

    @IdRes
    override fun getContentView(): Int = R.layout.activity_profile

    override fun renderInternal(sm: ProfileScreenModel) {
        profile_username_tv.text = sm.userName
    }

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): DefaultActivityScreenConfigurator = ProfileScreenConfigurator(intent)
}
