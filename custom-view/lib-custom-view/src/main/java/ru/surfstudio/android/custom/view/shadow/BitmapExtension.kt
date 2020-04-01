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
 * Creates padding the same size at all sides
 *
 * @param padding - the size of padding
 */
fun Bitmap.setPadding(padding: Int): Bitmap {
    return setPadding(padding, padding, padding, padding)
}

/**
 * Creates padding, allowing to tweak the size of padding for each side
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
