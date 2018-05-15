package ru.surfstudio.android.animations.anim

import android.support.v4.view.ViewPropertyAnimatorListener
import android.view.View


class DefaultViewPropertyAnimatorListener(predefinedAlpha: Float? = null,
                                          predefinedVisibility: Int? = null)
    : ViewPropertyAnimatorListener {

    private var initialAlpha: Float? = predefinedAlpha
    private var initialVisibility: Int? = predefinedVisibility

    override fun onAnimationStart(view: View) {
        if (initialAlpha == null) {
            initialAlpha = view.alpha
        }

        if (initialVisibility == null) {
            initialVisibility = view.visibility
        }
    }

    override fun onAnimationEnd(view: View) {
        // ignored
    }

    override fun onAnimationCancel(view: View) {
        view.alpha = initialAlpha ?: view.alpha
        view.visibility = initialVisibility ?: view.visibility
    }
}