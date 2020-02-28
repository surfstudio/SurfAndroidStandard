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
package ru.surfstudio.android.imageloader.data

import ru.surfstudio.android.imageloader.NO_SIZE

/**
 * Пакет с размерами изображения
 */
data class ImageSizeManager(
        var maxWidth: Int = NO_SIZE,    //максимальная ширина изображения в px
        var maxHeight: Int = NO_SIZE    //максимальная высота изображения в px
) {

    fun isMaxWidthSetUp() = maxWidth != NO_SIZE

    fun isMaxHeightSetUp() = maxHeight != NO_SIZE
}