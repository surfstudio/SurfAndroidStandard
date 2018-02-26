package ru.surfstudio.android.recycler.extension.sticky.layoutmanager

import android.support.design.widget.CoordinatorLayout
import android.view.View
import android.widget.FrameLayout

internal object Preconditions {

    fun <T> checkNotNull(item: T?, message: String): T {
        if (item == null) {
            throw NullPointerException(message)
        }
        return item
    }

    fun validateParentView(recyclerView: View) {
        val parentView = recyclerView.parent as View
        if (parentView !is FrameLayout && parentView !is CoordinatorLayout) {
            throw IllegalArgumentException("RecyclerView parent must be either a FrameLayout or CoordinatorLayout")
        }
    }
}