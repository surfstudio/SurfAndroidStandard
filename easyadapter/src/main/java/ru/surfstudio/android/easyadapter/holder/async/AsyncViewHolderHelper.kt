package ru.surfstudio.android.easyadapter.holder.async

import android.animation.*
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v4.view.AsyncLayoutInflater
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout

private const val SHARED_PREFERENCE_NAME = "async-view-holder"

internal fun inflateStubView(
        parent: ViewGroup,
        @LayoutRes stubLayoutId: Int,
        viewHolderName: String
): View {
    return FrameLayout(parent.context).apply {
        val height = getStubHeight(parent.context, viewHolderName)

        layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                if (height != -1) height else ViewGroup.LayoutParams.WRAP_CONTENT
        )

        LayoutInflater.from(parent.context).inflate(stubLayoutId, this, true)
    }
}

internal fun AsyncViewHolder.inflateItemView(
        itemView: View,
        @LayoutRes layoutId: Int,
        viewHolderName: String,
        endAction: () -> Unit = {}
) {
    itemView as FrameLayout

    AsyncLayoutInflater(itemView.context).inflate(layoutId, itemView) { view, _, _ ->
        val stubHeight = itemView.height
        val itemHeight = view.layoutParams.height

        saveStubHeight(itemView.context, viewHolderName, itemHeight)

        itemView.removeAllViews()
        fadeIn(itemView)
        itemView.addView(view)
        toSize(itemView, itemHeight, stubHeight)

        isItemViewInflated = true

        endAction()
    }
}

private fun AsyncViewHolder.fadeIn(view: View) {
    if (fadeInDuration == 0L) return

    val animatorListener = object : ViewPropertyAnimatorListener {
        private var initialAlpha: Float? = view.alpha
        private var initialVisibility: Int? = view.visibility

        override fun onAnimationStart(view: View) {
            if (initialAlpha == null) initialAlpha = view.alpha
            if (initialVisibility == null) initialVisibility = view.visibility
        }

        override fun onAnimationEnd(view: View) {}

        override fun onAnimationCancel(view: View) {
            view.alpha = initialAlpha ?: view.alpha
            view.visibility = initialVisibility ?: view.visibility
        }
    }

    with(view) {
        alpha = 0f
        visibility = View.VISIBLE
        clearAnimation()
    }

    ViewCompat.animate(view)
            .alpha(1f)
            .setDuration(fadeInDuration)
            .setInterpolator(FastOutLinearInInterpolator())
            .setListener(animatorListener)
            .withEndAction {
                fadeInAction()
            }
}

private fun AsyncViewHolder.toSize(itemView: ViewGroup,
                                   height: Int,
                                   oldHeight: Int) {
    if (resizeDuration == 0L || height == oldHeight) return

    val animator = ValueAnimator.ofInt(oldHeight, height).apply {
        duration = resizeDuration

        addUpdateListener {
            itemView.layoutParams.height = it.animatedValue as Int
            itemView.requestLayout()
        }

        addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                resizeAction()
            }
        })
    }

    AnimatorSet().apply {
        play(animator)
        interpolator = AccelerateDecelerateInterpolator()
        start()
    }
}

private fun saveStubHeight(context: Context, viewHolderName: String, height: Int) {
    context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit().apply {
        putInt(viewHolderName, height)
        apply()
    }
}

private fun getStubHeight(context: Context, viewHolderName: String): Int {
    return context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getInt(viewHolderName, -1)
}