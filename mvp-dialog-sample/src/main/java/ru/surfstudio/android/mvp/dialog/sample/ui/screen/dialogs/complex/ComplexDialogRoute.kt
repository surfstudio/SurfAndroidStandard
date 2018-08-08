package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex

import android.os.Bundle
import android.support.v4.app.DialogFragment
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogWithParamsRoute
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.data.SampleData

class ComplexDialogRoute(val sampleData: SampleData) : DialogWithParamsRoute() {

    override fun prepareBundle(): Bundle = Bundle().apply {
        putSerializable(Route.EXTRA_FIRST, sampleData)
    }

    override fun getFragmentClass(): Class<out DialogFragment> = ComplexDialogFragment::class.java
}