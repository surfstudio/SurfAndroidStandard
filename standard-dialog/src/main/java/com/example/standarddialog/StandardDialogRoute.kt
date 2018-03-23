package com.example.standarddialog

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogWithParamsRoute

/**
 * Route стандартного диалога
 */
class StandardDialogRoute(var title: String = "",
                          var message: String = "",
                          var positiveBtnText: String = "",
                          var negativeBtnText: String = "",
                          @StringRes var titleRes: Int = R.string.title,
                          @StringRes var messageRes: Int = R.string.message,
                          @StringRes var positiveBtnTextRes: Int = R.string.possitive_btn,
                          @StringRes var negativeBtnTextRes: Int = R.string.negative_btn,
                          var isCancelable: Boolean = true,
                          val dialogTag: String) : DialogWithParamsRoute() {

    constructor(bundle: Bundle) : this(bundle.getString(Route.EXTRA_FIRST),
            bundle.getString(Route.EXTRA_SECOND),
            bundle.getString(Route.EXTRA_THIRD),
            bundle.getString(Route.EXTRA_FOURTH),
            bundle.getInt(Route.EXTRA_FIFTH),
            bundle.getInt(Route.EXTRA_SIXTH),
            bundle.getInt(Route.EXTRA_SEVEN),
            bundle.getInt(Route.EXTRA_EIGHT),
            bundle.getBoolean(Route.EXTRA_NINE),
            bundle.getString(Route.EXTRA_TEN))

    override fun getFragmentClass() = StandardDialog::class.java

    override fun prepareBundle() = Bundle().apply {
        putSerializable(Route.EXTRA_FIRST, title)
        putSerializable(Route.EXTRA_SECOND, message)
        putSerializable(Route.EXTRA_THIRD, positiveBtnText)
        putSerializable(Route.EXTRA_FOURTH, negativeBtnText)
        putSerializable(Route.EXTRA_FIFTH, titleRes)
        putSerializable(Route.EXTRA_SIXTH, messageRes)
        putSerializable(Route.EXTRA_SEVEN, positiveBtnTextRes)
        putSerializable(Route.EXTRA_EIGHT, negativeBtnTextRes)
        putSerializable(Route.EXTRA_NINE, isCancelable)
        putSerializable(Route.EXTRA_TEN, dialogTag)
    }

    override fun getTag(): String {
        return dialogTag
    }

    fun getTitle(context: Context): String {
        return if (title.isEmpty()) {
            context.resources.getString(titleRes)
        } else {
            title
        }
    }

    fun getMessage(context: Context): String {
        return if (message.isEmpty()) {
            context.resources.getString(messageRes)
        } else {
            message
        }
    }

    fun getPositiveBtnTxt(context: Context): String {
        return if (positiveBtnText.isEmpty()) {
            context.resources.getString(positiveBtnTextRes)
        } else {
            positiveBtnText
        }
    }

    fun getNegativeBtnTxt(context: Context): String {
        return if (negativeBtnText.isEmpty()) {
            context.resources.getString(negativeBtnTextRes)
        } else {
            negativeBtnText
        }
    }
}