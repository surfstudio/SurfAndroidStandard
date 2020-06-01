/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.standarddialog

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogWithParamsRoute

const val EMPTY_STRING: String = ""

/**
 * Route стандартного диалога
 */
class StandardDialogRoute(var title: String = "",
                          var message: String = "",
                          var positiveBtnText: String = "",
                          var negativeBtnText: String = "",
                          @StringRes var titleRes: Int = -1,
                          @StringRes var messageRes: Int = R.string.message,
                          @StringRes var positiveBtnTextRes: Int = R.string.possitive_btn,
                          @StringRes var negativeBtnTextRes: Int = R.string.negative_btn,
                          var isCancelable: Boolean = true,
                          val dialogTag: String) : DialogWithParamsRoute() {

    constructor(bundle: Bundle) : this(bundle.getString(Route.EXTRA_FIRST) ?: EMPTY_STRING,
            bundle.getString(Route.EXTRA_SECOND) ?: EMPTY_STRING,
            bundle.getString(Route.EXTRA_THIRD) ?: EMPTY_STRING,
            bundle.getString(Route.EXTRA_FOURTH) ?: EMPTY_STRING,
            bundle.getInt(Route.EXTRA_FIFTH),
            bundle.getInt(Route.EXTRA_SIXTH),
            bundle.getInt(Route.EXTRA_SEVEN),
            bundle.getInt(Route.EXTRA_EIGHT),
            bundle.getBoolean(Route.EXTRA_NINE),
            bundle.getString(Route.EXTRA_TEN) ?: EMPTY_STRING)

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
        return if (title.isEmpty() && titleRes != -1) {
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