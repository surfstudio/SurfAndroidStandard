package ru.surfstudio.android.easyadapter.holder.async

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v4.view.AsyncLayoutInflater
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

const val DEFAULT_FADE_IN_DURATION = 200L
const val DEFAULT_RESIZE_DURATION = 200L

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
                if (height != UNDEFINE_HEIGHT) height else ViewGroup.LayoutParams.WRAP_CONTENT
        )

        LayoutInflater.from(itemView.context).inflate(stubLayoutId, this, true)
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
        fadeIn(itemView)
        itemView.addView(view)
        toSize(itemView, stubHeight, itemHeight)

        isItemViewInflated = true
        endAction()
        inflateFinish(view)
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
                fadeInFinish()
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