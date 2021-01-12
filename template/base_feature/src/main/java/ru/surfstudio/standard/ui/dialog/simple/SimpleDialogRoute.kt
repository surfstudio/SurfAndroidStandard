package ru.surfstudio.standard.ui.dialog.simple

import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import ru.surfstudio.android.core.mvi.impls.ui.dialog.standard.EMPTY_RES
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import ru.surfstudio.standard.ui.dialog.base.BaseSimpleDialogRoute

/**
 * Роут для открытия диалога возвращающего [SimpleResult]
 */
class SimpleDialogRoute(
        override val dialogId: String,
        @StringRes val titleRes: Int = EMPTY_RES,
        @StringRes val messageRes: Int = EMPTY_RES,
        @StringRes val positiveBtnTextRes: Int = R.string.positive_btn,
        @StringRes val negativeBtnTextRes: Int = R.string.negative_btn,
        val title: String = EMPTY_STRING,
        val message: String = EMPTY_STRING,
        val isCancelable: Boolean = false,
        @ColorRes val positiveBtnColorRes: Int = EMPTY_RES,
        @ColorRes val negativeBtnColorRes: Int = EMPTY_RES
) : BaseSimpleDialogRoute() {

    constructor(args: Bundle?) : this(
            args?.getString(Route.EXTRA_FIRST) ?: EMPTY_STRING,
            args?.getInt(Route.EXTRA_SECOND) ?: EMPTY_RES,
            args?.getInt(Route.EXTRA_THIRD) ?: EMPTY_RES,
            args?.getInt(Route.EXTRA_FOURTH) ?: EMPTY_RES,
            args?.getInt(Route.EXTRA_FIFTH) ?: EMPTY_RES,
            args?.getString(Route.EXTRA_SIXTH) ?: EMPTY_STRING,
            args?.getString(Route.EXTRA_SEVEN) ?: EMPTY_STRING,
            args?.getBoolean(Route.EXTRA_EIGHT) ?: false,
            args?.getInt(Route.EXTRA_NINE) ?: EMPTY_RES,
            args?.getInt(Route.EXTRA_TEN) ?: EMPTY_RES
    )

    override fun getScreenClass(): Class<out DialogFragment>? {
        return SimpleDialogView::class.java
    }

    override fun prepareData(): Bundle = Bundle().apply {
        putString(Route.EXTRA_FIRST, dialogId)
        putInt(Route.EXTRA_SECOND, titleRes)
        putInt(Route.EXTRA_THIRD, messageRes)
        putInt(Route.EXTRA_FOURTH, positiveBtnTextRes)
        putInt(Route.EXTRA_FIFTH, negativeBtnTextRes)
        putString(Route.EXTRA_SIXTH, title)
        putString(Route.EXTRA_SEVEN, message)
        putBoolean(Route.EXTRA_EIGHT, isCancelable)
        putInt(Route.EXTRA_NINE, positiveBtnColorRes)
        putInt(Route.EXTRA_TEN, negativeBtnColorRes)
    }
}
