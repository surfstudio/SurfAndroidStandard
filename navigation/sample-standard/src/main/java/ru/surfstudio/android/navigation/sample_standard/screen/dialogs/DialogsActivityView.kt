package ru.surfstudio.android.navigation.sample_standard.screen.dialogs

import android.os.Bundle
import android.os.PersistableBundle
import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.activity_dialogs.*
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView
import ru.surfstudio.android.navigation.sample_standard.R
import javax.inject.Inject

class DialogsActivityView : BaseRxActivityView() {

    @Inject
    lateinit var bm: DialogsBindModel

    override fun createConfigurator() = DialogsScreenConfigurator(intent)

    override fun getContentView(): Int = R.layout.activity_dialogs

    override fun getScreenName(): String = "Dialogs"

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        dialogs_default_btn.clicks() bindTo { bm.openDialogButtonClicked.accept() }
        dialogs_fade_btn.clicks() bindTo { bm.openDialogWithFadeButtonClicked.accept() }
        dialogs_slide_btn.clicks() bindTo { bm.openDialogWithSlideButtonClicked.accept() }
    }
}