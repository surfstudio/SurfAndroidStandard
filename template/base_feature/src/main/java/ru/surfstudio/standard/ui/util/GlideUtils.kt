package ru.surfstudio.standard.ui.util

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

/** Слушатель загрузки изображений с помощью Glide */
fun glideListener(
    onImageLoadedLambda: ((drawable: Drawable?) -> (Unit)) = { },
    onImageLoadErrorLambda: ((throwable: Throwable?) -> (Unit)) = { }
): RequestListener<Drawable> {
    return object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            onImageLoadErrorLambda(e)
            return false
        }

        override fun onResourceReady(
            resource: Drawable,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onImageLoadedLambda(resource)
            return false
        }
    }
}