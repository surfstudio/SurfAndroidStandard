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

/**
 * Синхронный инфлейт stub-view которая отображается перед инфлейтом основной
 */
internal fun inflateStubView(itemView: ViewGroup, @LayoutRes stubLayoutId: Int): View {
    return LayoutInflater.from(itemView.context).inflate(stubLayoutId, itemView, true)
}

/**
 * Асинхронный инфлейт основной-view
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
internal fun AsyncViewHolder.inflateItemView(
        itemView: ViewGroup,
        @LayoutRes layoutId: Int,
        endAction: () -> Unit = {}
) {
    itemView as FrameLayout

    AsyncLayoutInflater(itemView.context).inflate(layoutId, itemView) { view, _, _ ->
        itemView.removeAllViews()

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

        itemView.addView(view)

        isItemViewInflated = true
        onViewInflated(view)
        endAction()
    }
}

/**
 * Контейнер который служит для замены stub-view на основную
 */
internal fun getContainer(parent: ViewGroup, containerWidth: Int, containerHeight: Int): ViewGroup {
    return FrameLayout(parent.context).apply {
        layoutParams = ViewGroup.LayoutParams(
                containerWidth,
                containerHeight
        )
    }
}