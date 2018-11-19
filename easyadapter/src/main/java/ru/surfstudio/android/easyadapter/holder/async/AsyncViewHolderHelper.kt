package ru.surfstudio.android.easyadapter.holder.async

import android.annotation.TargetApi
import android.os.Build
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.asynclayoutinflater.view.AsyncLayoutInflater

const val DEFAULT_FADE_IN_DURATION = 200L

const val DEFAULT_WIDTH = ViewGroup.LayoutParams.WRAP_CONTENT
const val DEFAULT_HEIGHT = ViewGroup.LayoutParams.WRAP_CONTENT

internal fun inflateStubView(itemView: ViewGroup, @LayoutRes stubLayoutId: Int): View {
    return LayoutInflater.from(itemView.context).inflate(stubLayoutId, itemView, true)
}

@TargetApi(Build.VERSION_CODES.KITKAT)
internal fun AsyncViewHolder.inflateItemView(
        itemView: ViewGroup,
        @LayoutRes layoutId: Int,
        endAction: () -> Unit = {}
) {
    itemView as FrameLayout

    AsyncLayoutInflater(itemView.context).inflate(layoutId, itemView) { view, _, _ ->
        itemView.removeAllViews()

        itemView.addView(view)

        if (fadeInDuration != 0L) {
            TransitionManager.beginDelayedTransition(
                    itemView,
                    Fade(Fade.IN).apply {
                        duration = fadeInDuration
                        addListener(object : Transition.TransitionListener {
                            override fun onTransitionStart(transition: Transition?) {}
                            override fun onTransitionResume(transition: Transition?) {}
                            override fun onTransitionPause(transition: Transition?) {}
                            override fun onTransitionCancel(transition: Transition?) {}
                            override fun onTransitionEnd(transition: Transition?) {
                                onFadeInEnd()
                            }
                        })
                    }
            )
        }

        isItemViewInflated = true
        onViewInflated(view)
        endAction()
    }
}

internal fun getContainer(parent: ViewGroup, containerWidth: Int, containerHeight: Int): ViewGroup {
    return FrameLayout(parent.context).apply {
        layoutParams = ViewGroup.LayoutParams(
                containerWidth,
                containerHeight
        )
    }
}