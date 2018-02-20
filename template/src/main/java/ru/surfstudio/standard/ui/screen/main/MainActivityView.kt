package ru.surfstudio.standard.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.find
import ru.surfstudio.android.core.ui.base.screen.activity.BaseRenderableHandleableErrorActivityView
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter
import ru.surfstudio.standard.R
import ru.surfstudio.standard.ui.base.configurator.ActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableHandleableErrorActivityView<MainScreenModel>() {

    @Inject
    internal lateinit var presenter: MainPresenter

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

        val openCameraBtn: Button = find(R.id.camera_btn)
        openCameraBtn.setOnClickListener { presenter.openCamera() }
        val openGalleryBtn: Button = find(R.id.gallery_btn)
        openGalleryBtn.setOnClickListener { presenter.openGallerySingle() }
        val openGalleryMBtn: Button = find(R.id.gallery_m_btn)
        openGalleryMBtn.setOnClickListener { presenter.openGalleryMultiple() }
    }

    override fun renderInternal(screenModel: MainScreenModel) {

    }

    override fun onResume() {
        super.onResume()
        camera_preview.start()
    }

    override fun onPause() {
        super.onPause()
        camera_preview.stop()
    }
}
