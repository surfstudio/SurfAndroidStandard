package ru.surfstudio.android.core.mvi.impls.ui.dialog.standard

import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.impls.R
import ru.surfstudio.android.core.mvi.impls.ui.dialog.EventHubDialogComponent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogWithParamsRoute
import java.io.Serializable

const val EMPTY_RES = -1

/**
 * Route for customizable alert dialog view.
 *
 * If you need do do some action on button clicks or dialog dismiss -
 * you need to pass this events to route constructor.
 *
 * To emit events from this dialog, you need to inherit parent component from [EventHubDialogComponent]
 */
class StandardReactDialogRoute<E : Event>(
        @StringRes var titleRes: Int = EMPTY_RES,
        @StringRes var messageRes: Int = EMPTY_RES,
        @StringRes var positiveBtnTextRes: Int = R.string.positive_btn,
        @StringRes var negativeBtnTextRes: Int = R.string.negative_btn,
        var title: String = "",
        var message: String = "",
        var isCancelable: Boolean = false,
        val positiveButtonEvent: E? = null,
        val negativeButtonEvent: E? = null,
        val dismissEvent: E? = null,
        @ColorRes val positiveBtnColorRes: Int = EMPTY_RES,
        @ColorRes val negativeBtnColorRes: Int = EMPTY_RES
) : DialogWithParamsRoute() {

    constructor(args: Bundle?) : this(
            args?.getInt(Route.EXTRA_FIRST) ?: EMPTY_RES,
            args?.getInt(Route.EXTRA_SECOND) ?: EMPTY_RES,
            args?.getInt(Route.EXTRA_THIRD) ?: EMPTY_RES,
            args?.getInt(Route.EXTRA_FOURTH) ?: EMPTY_RES,
            args?.getString(Route.EXTRA_FIFTH) ?: "",
            args?.getString(Route.EXTRA_SIXTH) ?: "",
            args?.getBoolean(Route.EXTRA_SEVEN) ?: false,
            args?.getSerializable(Route.EXTRA_EIGHT) as? E,
            args?.getSerializable(Route.EXTRA_NINE) as? E,
            args?.getSerializable(Route.EXTRA_TEN) as? E,
            args?.getInt(Route.EXTRA_ELEVEN) ?: EMPTY_RES,
            args?.getInt(Route.EXTRA_TWELVE) ?: EMPTY_RES
    )

    override fun getFragmentClass(): Class<out DialogFragment> = StandardReactDialogView::class.java

    override fun getTag(): String = "StandardReactDialogView"

    override fun prepareBundle(): Bundle = Bundle().apply {
        putInt(Route.EXTRA_FIRST, titleRes)
        putInt(Route.EXTRA_SECOND, messageRes)
        putInt(Route.EXTRA_THIRD, positiveBtnTextRes)
        putInt(Route.EXTRA_FOURTH, negativeBtnTextRes)
        putString(Route.EXTRA_FIFTH, title)
        putString(Route.EXTRA_SIXTH, message)
        putBoolean(Route.EXTRA_SEVEN, isCancelable)
        putSerializable(Route.EXTRA_EIGHT, positiveButtonEvent as? Serializable)
        putSerializable(Route.EXTRA_NINE, negativeButtonEvent as? Serializable)
        putSerializable(Route.EXTRA_TEN, dismissEvent as? Serializable)
        putInt(Route.EXTRA_ELEVEN, positiveBtnColorRes)
        putInt(Route.EXTRA_TWELVE, negativeBtnColorRes)
    }
}