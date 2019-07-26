/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

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
 * Synchronous inflate for stub-View which would be displayed before the main View.
 *
 * @param itemView parent for the stub View
 * @param stubLayoutId layout id of the stub View
 */
internal fun inflateStubView(itemView: ViewGroup, @LayoutRes stubLayoutId: Int): View {
    return LayoutInflater.from(itemView.context).inflate(stubLayoutId, itemView, true)
}

/**
 * Asynchronous inflate of the main View.
 *
 * @param itemView parent for the main View
 * @param layoutId layout id of the main View
 * @param endAction action that will be invoked after inflate is finished
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
 * Container to hold the stub View and the main View.
 */
internal fun getContainer(parent: ViewGroup, containerWidth: Int, containerHeight: Int): ViewGroup {
    return FrameLayout(parent.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            containerWidth,
            containerHeight
        )
    }
}