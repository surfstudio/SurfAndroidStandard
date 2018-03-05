package com.example.standarddialog

import android.os.Bundle
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogWithParamsRoute

/**
 * Route стандартного диалога
 */
class StandardDialogRoute(var title: String,
                          var message: String,
                          var possitiveBtnText: String,
                          var negativeBtnText: String,
                          var isCancelable: Boolean = true,
                          val dialogTag: String) : DialogWithParamsRoute() {


    constructor(bundle: Bundle) : this(bundle.getString(Route.EXTRA_FIRST),
            bundle.getString(Route.EXTRA_SECOND),
            bundle.getString(Route.EXTRA_THIRD),
            bundle.getString(Route.EXTRA_FOURTH),
            bundle.getBoolean(Route.EXTRA_FIFTH),
            bundle.getString(Route.EXTRA_SIXTH))

    override fun getFragmentClass() = StandardDialog::class.java

    override fun prepareBundle() = Bundle().apply {
        putSerializable(Route.EXTRA_FIRST, title)
        putSerializable(Route.EXTRA_SECOND, message)
        putSerializable(Route.EXTRA_THIRD, possitiveBtnText)
        putSerializable(Route.EXTRA_FOURTH, negativeBtnText)
        putSerializable(Route.EXTRA_FIFTH, isCancelable)
        putSerializable(Route.EXTRA_SIXTH, dialogTag)
    }

    override fun getTag(): String {
        return dialogTag
    }
}