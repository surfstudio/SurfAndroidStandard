package ru.surfstudio.android.easyadapter.holder.async

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.recyclerview.widget.RecyclerView

const val DEFAULT_FADE_IN_DURATION = 200L
const val DEFAULT_RESIZE_DURATION = 1200L

private const val SHARED_PREFERENCE_NAME = "async-view-holder"
private const val UNDEFINE_HEIGHT = -1

internal fun AsyncViewHolder.inflateStubView(
        itemView: ViewGroup,
        @LayoutRes stubLayoutId: Int
): View {
    return itemView.apply {
        val height = getStubHeight(itemView.context, holderKey)

        layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val stubView = LayoutInflater.from(itemView.context).inflate(stubLayoutId, this, false).apply {
            if (height != UNDEFINE_HEIGHT) layoutParams.height = height
        }
        addView(stubView)
    }
}

internal fun AsyncViewHolder.inflateItemView(
        parent: ViewGroup,
        itemView: View,
        @LayoutRes layoutId: Int,
        endAction: () -> Unit = {}
) {
    itemView as FrameLayout

    AsyncLayoutInflater(itemView.context).inflate(layoutId, itemView) { view, _, _ ->
        val stubHeight = itemView.height
        val itemHeight = calcItemHeight(parent as RecyclerView, view)

        saveStubHeight(itemView.context, holderKey, itemHeight)

        itemView.removeAllViews()
        fadeIn(view)
        itemView.addView(view)
        toSize(itemView, stubHeight, itemHeight)?.start()

        isItemViewInflated = true
        endAction()
        onViewInflated(view)
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
                onFadeInEnd()
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
            .getInt(viewHolderName, UNDEFINE_HEIGHT)
}

private fun calcItemHeight(recyclerView: RecyclerView, view: View) = when (view.layoutParams.height) {
    ViewGroup.LayoutParams.MATCH_PARENT -> {
        view.measure(view.layoutParams.width, ViewGroup.LayoutParams.MATCH_PARENT)
        recyclerView.height

    }
    ViewGroup.LayoutParams.WRAP_CONTENT -> {
        view.measure(view.layoutParams.width, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.measuredHeight
    }
    else -> view.layoutParams.height
}

internal fun AsyncViewHolder.resize(stubView: ViewGroup,
                                    oldHeight: Int,
                                    height: Int): AnimatorSet? {
    if (resizeDuration == 0L || height == oldHeight) return null

    val animator = ValueAnimator.ofInt(oldHeight, height).apply {
        duration = resizeDuration

        addUpdateListener {
            stubView.layoutParams.height = it.animatedValue as Int
            stubView.requestLayout()
        }
    }

    return AnimatorSet().apply {
        play(animator)
        interpolator = AccelerateDecelerateInterpolator()
    }
}