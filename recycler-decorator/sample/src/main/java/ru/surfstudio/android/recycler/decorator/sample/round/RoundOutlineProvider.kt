/*
 * Copyright 2019 Oleg Zhilo
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.surfstudio.android.recycler.decorator.sample.round

import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi

/**
 * [ViewOutlineProvider] witch works with [RoundMode]
 * @param outlineRadius corner radius
 * @param roundMode mode for corners
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class RoundOutlineProvider(
        var outlineRadius: Float = 0f,
        var roundMode: RoundMode = RoundMode.NONE
) : ViewOutlineProvider() {

    private val topOffset
        get() = when (roundMode) {
            RoundMode.ALL, RoundMode.TOP -> 0
            RoundMode.NONE, RoundMode.BOTTOM -> cornerRadius.toInt()
        }
    private val bottomOffset
        get() = when (roundMode) {
            RoundMode.ALL, RoundMode.BOTTOM -> 0
            RoundMode.NONE, RoundMode.TOP -> cornerRadius.toInt()
        }
    private val cornerRadius
        get() = if (roundMode == RoundMode.NONE) {
            0f
        } else {
            outlineRadius
        }

    override fun getOutline(view: View, outline: Outline) {
        outline.setRoundRect(
                0,
                0 - topOffset,
                view.width,
                view.height + bottomOffset,
                cornerRadius
        )
    }
}