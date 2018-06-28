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
package ru.surfstudio.android.message

import android.graphics.Color
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.provider.FragmentProvider

/**
 * Базовый класс контроллера отображения сообщений
 * Для нахождения view использует fragment и затем activity провайдеры
 */
class DefaultMessageController @JvmOverloads constructor(
        val activityProvider: ActivityProvider,
        val fragmentProvider: FragmentProvider? = null)
    : MessageController {

    private val ILLEGAL_COLOR = Color.TRANSPARENT

    private var snackBarBackgroundColor: Int? = null
    private var toast: Toast? = null

    init {
        val typedArray = activityProvider.get()
                .obtainStyledAttributes(intArrayOf(R.attr.snackBarBackgroundColor))
        try {
            val color = typedArray.getColor(0, ILLEGAL_COLOR)
            if (color != ILLEGAL_COLOR) {
                snackBarBackgroundColor = color
            }
        } catch (ignored: UnsupportedOperationException) {
            // ignored
        } finally {
            typedArray.recycle()
        }
    }

    override fun show(stringId: Int,
                      backgroundColor: Int?,
                      actionStringId: Int?,
                      buttonColor: Int?,
                      duration: Int,
                      listener: (view: View) -> Unit) {
        show(getView().resources.getString(stringId), backgroundColor, actionStringId, buttonColor, duration, listener)
    }

    override fun show(message: String,
                      backgroundColor: Int?,
                      actionStringId: Int?,
                      buttonColor: Int?,
                      duration: Int,
                      listener: (view: View) -> Unit) {
        val view = getView()
        val sb = Snackbar.make(view, message, duration)

        if (backgroundColor == null) {
            snackBarBackgroundColor?.let {
                sb.view.setBackgroundColor(it)
            }
        } else {
            sb.view.setBackgroundColor(ContextCompat.getColor(view.context, backgroundColor))
        }
        actionStringId?.let {
            sb.setAction(it) { view -> listener.invoke(view) }
        }
        buttonColor?.let {
            sb.setActionTextColor(ContextCompat.getColor(view.context, it))
        }
        sb.show()
    }

    override fun showToast(stringId: Int, gravity: Int, duration: Int) {
        showToast(getView().resources.getString(stringId), gravity, duration)
    }

    override fun showToast(message: String, gravity: Int, duration: Int) {
        toast?.cancel()
        toast = Toast.makeText(getView().context, message, duration)
                .apply {
                    setGravity(gravity, 0, 0)
                    show()
                }
    }

    /**
     * Порядок поиска подходящей корневой вью для SnackBar происходит со следующим приоритетом:
     * R.id.snackbar_container во фрагменте, должен быть FrameLayout
     * R.id.coordinator во фрагменте, должен быть CoordinatorLayout
     * R.id.snackbar_container в активитиб должен быть CoordinatorLayout или FrameLayout
     * R.id.coordinator в активити, должен быть CoordinatorLayout
     * android.R.id.content активити
     *
     *
     * Для того, чтобы срабатывал Behavior на появление Snackbar,
     * нужно чтобы найденая View была [CoordinatorLayout]
     */
    protected fun getView(): View {
        return getViewFromFragment(fragmentProvider)
                ?: getViewFromActivity(activityProvider)
                ?: throw ViewForSnackbarNotFoundException("ViewForSnackbarNotFound")
    }

    private fun getViewFromFragment(fragmentProvider: FragmentProvider?): View? {
        val fragmentView = fragmentProvider?.get()?.view ?: return null
        return fragmentView.findViewById(R.id.snackbar_container)
                ?: fragmentView.findViewById(R.id.coordinator)
    }

    private fun getViewFromActivity(activityProvider: ActivityProvider): View? {
        val activity = activityProvider.get()
        return activity.findViewById(R.id.snackbar_container)
                ?: activity.findViewById(R.id.coordinator)
                ?: activity.findViewById(android.R.id.content)
    }
}