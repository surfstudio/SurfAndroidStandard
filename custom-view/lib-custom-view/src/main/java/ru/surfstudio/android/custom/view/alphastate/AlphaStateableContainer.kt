/*
  Copyright (c) 2020-present, SurfStudio LLC.

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

import android.view.ViewGroup

internal const val STATE_PRESSED_ALPHA = 0.7f
internal const val STATE_NORMAL_ALPHA = 1f

/**
 * Коонтейнер со свойством подмены прозрачности в зависимости от состояния (state_pressed)
 * По умолчанию при pressed_state вью получает 70% прозрачности.
 *
 * @property viewGroup текущий вюгруп
 * @property statePressedAlpha прозрачность при нажатом состоянии
 * @property stateNormalAlpha прозрачность при нормальном состоянии
 */
interface AlphaStateableContainer {

    var viewGroup: ViewGroup?
    var statePressedAlpha: Float
    var stateNormalAlpha: Float

    fun changeAlpha()
}