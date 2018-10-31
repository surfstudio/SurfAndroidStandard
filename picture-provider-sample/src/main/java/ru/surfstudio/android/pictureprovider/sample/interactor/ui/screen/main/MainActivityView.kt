package ru.surfstudio.android.pictureprovider.sample.interactor.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.find
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.pictureprovider.sample.R
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {
    override fun getScreenName(): String  = "MainActivity"

    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        configureButton(R.id.camera_btn) { presenter.openCamera() }
        configureButton(R.id.gallery_btn) { presenter.openGallerySingle()}
        configureButton(R.id.gallery_m_btn) { presenter.openGalleryMultiple() }
        configureButton(R.id.chooser_btn) { presenter.openChooserSingle() }
        configureButton(R.id.chooser_m_btn) { presenter.openChooserMultiple() }
        configureButton(R.id.chooser_save_btn) { presenter.openChooserAndSavePhoto() }
    }

    override fun renderInternal(sm: MainScreenModel) {}

    override fun getPresenters(): Array<CorePresenter<*>> {
        return arrayOf(presenter)
    }

    override fun createConfigurator(): DefaultActivityScreenConfigurator {
        return MainScreenConfigurator(intent)
    }

    private fun configureButton(@IdRes buttonId: Int, onClickListener: (view: View) -> Unit) {
        val button: Button = find(buttonId)
        button.setOnClickListener { onClickListener(it) }
    }

    fun startCamera() {
        camera_preview.start()
    }

    fun stopCamera() {
        camera_preview.stop()
    }
}
