package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.result

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogWithResultRoute

class ResultDialogRoute : DialogWithResultRoute<String>() {
    override fun getFragmentClass(): Class<out DialogFragment> = ResultDialogView::class.java
    override fun prepareBundle(): Bundle = Bundle.EMPTY
}