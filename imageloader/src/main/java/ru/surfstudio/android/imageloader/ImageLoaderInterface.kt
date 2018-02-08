package ru.surfstudio.android.imageloader

import android.widget.ImageView
import java.io.IOException

interface ImageLoaderInterface {

    /**
     * Загрузка изображения из сети
     *
     * @param url сетевая ссылка на изображение
     */
    @Throws(IllegalArgumentException::class)
    fun url(url: String): ImageLoader

    /**
     * Указание целевой [ImageView]
     *
     * @param imageView экземпляр [ImageView] для загрузки изображения
     */
    fun into(imageView: ImageView)
}