package ru.surfstudio.android.location.dialog

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithParamsAndResultRoute


class LocationDeniedDialogRoute(val data: LocationDeniedDialogData) : ActivityWithParamsAndResultRoute<Boolean>() {

    override fun prepareIntent(context: Context): Intent {
        val intent = Intent(context, LocationDeniedDialogView::class.java)
        intent.putExtra(EXTRA_FIRST, data)
        return intent
    }

}
