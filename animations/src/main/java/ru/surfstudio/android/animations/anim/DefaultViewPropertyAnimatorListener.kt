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
package ru.surfstudio.android.animations.anim

import androidx.core.view.ViewPropertyAnimatorListener
import android.view.View

/**
 * ViewPropertyAnimatorListener враппер,
 * который умеет сбрасывать состояниие вьюшки
 * если анимация была отменена
 */
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