/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.surfstudio.android.custom.view.shadow

import android.graphics.Bitmap
import android.graphics.Canvas
import ru.surfstudio.android.imageloader.util.BlurUtil

/**
 * Blurs the Bitmap using [BlurUtil.stackBlur]
 */
fun Bitmap.stackBlur(blurRadius: Int = 1, canReuseBitmap: Boolean = false): Bitmap {
    require(blurRadius >= 1) { "Radius has to be > 1, current radius: $blurRadius" }
    val blurredBitmap = BlurUtil.stackBlur(this, blurRadius, canReuseBitmap)
    return blurredBitmap ?: this
}

/**
 * Creates bitmap with padding. Each side of initial bitmap will be padded with [padding].
 *
 * @param padding - the size of padding (px)
 */
fun Bitmap.setPadding(padding: Int): Bitmap {
    return setPadding(padding, padding, padding, padding)
}

/**
 * Creates bitmap with padding. Padding values on each side can be tweaked.
 */
fun Bitmap.setPadding(
        leftPadding: Int = 0,
        rightPadding: Int = 0,
        topPadding: Int = 0,
        bottomPadding: Int = 0
): Bitmap {
    if (leftPadding == 0 && rightPadding == 0 && topPadding == 0 && bottomPadding == 0) return this

    val outputBitmap = Bitmap.createBitmap(
            width + leftPadding + rightPadding,
            height + topPadding + bottomPadding,
            Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(outputBitmap)

    canvas.drawBitmap(this, leftPadding.toFloat(), topPadding.toFloat(), null)
    return outputBitmap
}
