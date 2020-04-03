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
package ru.surfstudio.android.custom.view.alphastate

import android.animation.ValueAnimator
import android.view.ViewGroup
import ru.surfstudio.android.animations.anim.changeValue

internal const val ALPHA_ANIMATION_DURATION = 250L

/**
 * Дефолтная реализация интерфейса [AlphaStateableContainer]
 */
class DefaultAlphaStateableContainer : AlphaStateableContainer {

    private var currentAnimation: ValueAnimator? = null

    override var viewGroup: ViewGroup? = null

    override var stateNormalAlpha = STATE_NORMAL_ALPHA
        set(value) {
            field = value
            changeAlpha()
        }

    override var statePressedAlpha = STATE_PRESSED_ALPHA
        set(value) {
            field = value
            changeAlpha()
        }

    override fun changeAlpha() {
        if (viewGroup?.drawableState?.contains(android.R.attr.state_pressed) == true) {
            animateAlpha(statePressedAlpha)
        } else {
            animateAlpha(stateNormalAlpha)
        }
    }

    private fun animateAlpha(endAlpha: Float) {
        currentAnimation?.cancel()
        currentAnimation = changeValue(
                from = viewGroup?.alpha ?: endAlpha,
                to = endAlpha,
                duration = ALPHA_ANIMATION_DURATION,
                update = {
                    viewGroup?.alpha = it
                }
        )
    }
}