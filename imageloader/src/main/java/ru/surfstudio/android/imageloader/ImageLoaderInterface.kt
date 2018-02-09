package ru.surfstudio.android.imageloader

import android.support.annotation.DrawableRes
import android.widget.ImageView

interface ImageLoaderInterface {

    /**
     * Загрузка изображения из сети
     *
     * @param url сетевая ссылка на изображение
     */
    @Throws(IllegalArgumentException::class)
    fun url(url: String): ImageLoader

    /**
     * Загрузка изображения из ресурсов
     *
     * @param drawableResId ссылка на ресурс из папки res/drawable
     */
    fun url(@DrawableRes drawableResId: Int): ImageLoader

    /**
     * Указание графического ресурса, отображаемого в случае ошибки загрузки
     *
     * @param drawableResId ссылка на ресурс из папки res/drawable
     */
    fun error(@DrawableRes drawableResId: Int): ImageLoader

    /**
     * Указание целевой [ImageView]
     *
     * @param imageView экземпляр [ImageView] для загрузки изображения
     */
    fun into(imageView: ImageView)
}