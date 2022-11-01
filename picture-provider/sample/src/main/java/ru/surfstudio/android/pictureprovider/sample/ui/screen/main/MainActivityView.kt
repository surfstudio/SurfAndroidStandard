package ru.surfstudio.android.pictureprovider.sample.ui.screen.main

import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.IdRes
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.pictureprovider.sample.R
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {

    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(
            savedInstanceState: Bundle?,
            persistentState: PersistableBundle?,
            viewRecreated: Boolean
    ) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        camera_btn.setOnClickListener { presenter.openCamera() }
        camera_uri_btn.setOnClickListener { presenter.openCameraUri() }
        gallery_btn.setOnClickListener { presenter.openGallerySingle() }
        gallery_m_btn.setOnClickListener { presenter.openGalleryMultiple() }
        chooser_btn.setOnClickListener { presenter.openChooserSingle() }
        chooser_m_btn.setOnClickListener { presenter.openChooserMultiple() }
        chooser_save_btn.setOnClickListener { presenter.openChooserAndSavePhoto() }
    }

    override fun renderInternal(sm: MainScreenModel) {
        val photoUri = sm.photoUri
        if (photoUri != null) {
            showImage(photoUri)
        }
    }

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): DefaultActivityScreenConfigurator = MainScreenConfigurator(intent)

    override fun getScreenName(): String = "MainActivity"

    private fun showImage(photoUri: Uri) {
        example_iv.setImageURI(photoUri)
    }
}
