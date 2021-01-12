package ru.surfstudio.standard.ui.dialog.simple

import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.impls.ui.dialog.standard.EMPTY_RES
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogWithParamsRoute
import ru.surfstudio.android.navigation.route.dialog.DialogRoute
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

/**
 * Роут простого диалога с заголовком, сообщением, позитивной и негативной кнопкой [SimpleDialogView]
 *
 * В случае, если надо выполнять какое-то действие по нажатию на кнопки или скрытие диалога -
 * необходимо передавать эти события в конструктор.
 * Для добавления этого диалога на экран, необходимо унаследовать родительский компонент от [SimpleEventHubDialogComponent]
 * и запровайдить родительский ScreenEventHub<T> как ScreenEventHub<Event>
 *
 * *Важно:* параметры типов событий:
 * positiveButtonEvent, negativeButtonEvent, dismissEvent
 * должны реализовать [Serializable]
 */
@Suppress("UNCHECKED_CAST")
class SimpleDialogRoute<E : Event>(
        @StringRes var titleRes: Int = EMPTY_RES,
        @StringRes var messageRes: Int = EMPTY_RES,
        @StringRes var positiveBtnTextRes: Int = EMPTY_RES,
        @StringRes var negativeBtnTextRes: Int = EMPTY_RES,
        @ColorRes var positiveBtnColorRes: Int = R.color.colorAccent,
        var title: String = EMPTY_STRING,
        var message: String = EMPTY_STRING,
        var isCancelable: Boolean = true,
        val positiveButtonEvent: E? = null,
        val negativeButtonEvent: E? = null,
        val dismissEvent: E? = null,
        val isNegativeButtonVisible: Boolean = true
) : DialogRoute() {

    constructor(args: Bundle) : this(
            args.getInt(Route.EXTRA_FIRST),
            args.getInt(Route.EXTRA_SECOND),
            args.getInt(Route.EXTRA_THIRD),
            args.getInt(Route.EXTRA_FOURTH),
            args.getInt(Route.EXTRA_FIFTH),
            args.getString(Route.EXTRA_SIXTH) ?: EMPTY_STRING,
            args.getString(Route.EXTRA_SEVEN) ?: EMPTY_STRING,
            args.getBoolean(Route.EXTRA_EIGHT),
            args.getSerializable(Route.EXTRA_NINE) as? E,
            args.getSerializable(Route.EXTRA_TEN) as? E,
            args.getSerializable(Route.EXTRA_ELEVEN) as? E,
            args.getBoolean(Route.EXTRA_TWELVE)
    )

    override fun getScreenClass(): Class<out DialogFragment> = SimpleDialogView::class.java

    override fun getId(): String = "SimpleDialogView"

    override fun prepareData(): Bundle = bundleOf(
            Route.EXTRA_FIRST to titleRes,
            Route.EXTRA_SECOND to messageRes,
            Route.EXTRA_THIRD to positiveBtnTextRes,
            Route.EXTRA_FOURTH to negativeBtnTextRes,
            Route.EXTRA_FIFTH to positiveBtnColorRes,
            Route.EXTRA_SIXTH to title,
            Route.EXTRA_SEVEN to message,
            Route.EXTRA_EIGHT to isCancelable,
            Route.EXTRA_NINE to positiveButtonEvent,
            Route.EXTRA_TEN to negativeButtonEvent,
            Route.EXTRA_ELEVEN to dismissEvent,
            Route.EXTRA_TWELVE to isNegativeButtonVisible
    )
}