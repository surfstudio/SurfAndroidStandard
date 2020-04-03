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
package ru.surfstudio.android.custom.view.alphastate.impls

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import ru.surfstudio.android.custom.view.R
import ru.surfstudio.android.custom.view.alphastate.AlphaStateableContainer
import ru.surfstudio.android.custom.view.alphastate.DefaultAlphaStateableContainer
import ru.surfstudio.android.custom.view.alphastate.STATE_NORMAL_ALPHA
import ru.surfstudio.android.custom.view.alphastate.STATE_PRESSED_ALPHA

/**
 * @see AlphaStateableContainer
 */
class AlphaStateableLinearLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs), AlphaStateableContainer by DefaultAlphaStateableContainer() {

    init {
        viewGroup = this
        initAttrs(attrs)
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        changeAlpha()
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.AlphaStateableLinearLayout, 0, 0)
            statePressedAlpha = a.getFloat(R.styleable.AlphaStateableLinearLayout_statePressedAlpha, STATE_PRESSED_ALPHA)
            stateNormalAlpha = a.getFloat(R.styleable.AlphaStateableLinearLayout_stateNormalAlpha, STATE_NORMAL_ALPHA)
            a.recycle()
        }
    }
}