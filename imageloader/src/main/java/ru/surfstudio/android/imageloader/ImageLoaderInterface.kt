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
package ru.surfstudio.android.imageloader

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.Shader
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.WorkerThread
import android.view.View
import ru.surfstudio.android.imageloader.data.CacheStrategy
import ru.surfstudio.android.imageloader.data.ImageSource
import ru.surfstudio.android.imageloader.transformations.RoundedCornersTransformation.CornerType
import ru.surfstudio.android.imageloader.util.BlurStrategy

/**
 * Универсальный интерфейс загрузчика изображений.
 */
interface ImageLoaderInterface {

    /**
     * Загрузка изображения из сети
     *
     * @param url сетевая ссылка на изображение
     */
    fun url(url: String): ImageLoaderInterface

    /**
     * Загрузка изображения из ресурсов
     *
     * @param drawableResId ссылка на ресурс из папки res/drawable
     */
    fun url(@DrawableRes drawableResId: Int): ImageLoaderInterface

    /**
     * Указание графического ресурса, отображаемого в качестве плейсхолдера
     *
     * @param drawableResId ссылка на ресурс из папки res/drawable
     */
    fun preview(@DrawableRes drawableResId: Int): ImageLoaderInterface

    /**
     * Указание графического ресурса, отображаемого в случае ошибки загрузки
     *
     * @param drawableResId ссылка на ресурс из папки res/drawable
     */
    fun error(@DrawableRes drawableResId: Int): ImageLoaderInterface

    /**
     * Установка лямбды для отслеживания загрузки изображения
     *
     * @param lambda лямбда, возвращающая загруженный [Drawable] и [ImageSource], указывающий откуда он был загружен
     */
    fun listener(lambda: ((drawable: Drawable, imageSource: ImageSource?) -> (Unit))): ImageLoaderInterface

    /**
     * Установка лямбды для отслеживания ошибки при загрузке изображения
     *
     * @param lambda лямбда, возвращающая ошибку [Throwable]
     */
    fun errorListener(lambda: ((throwable: Throwable) -> (Unit))): ImageLoaderInterface

    /**
     * Указание политики кэширования.
     * Метод предоставляет возможность настроить кеширование загруженных изображений на диске.
     *
     * @param cacheStrategy необходимая стратегия кеширования
     */
    fun cacheStrategy(cacheStrategy: CacheStrategy): ImageLoaderInterface

    /**
     * Указание возможности полного пропуска кеширования изображения
     * Метод предоставляет возможность отключить кэширование загруженных изображений в памяти и на диске.
     * В случае указания политики кеширования, она перезапишется
     *
     * @param skipCache true - игнорировать кэш в памяти и на диске, false - использовать кэш в памяти и на диске
     */
    fun skipCache(skipCache: Boolean = true): ImageLoaderInterface

    /**
     * Установка максимальной ширины изображения в px.
     *
     * Необходима для пережатия изображения без искажения пропорций.
     */
    fun maxWidth(maxWidth: Int): ImageLoaderInterface

    /**
     * Установка максимальной высоты изображения в px.
     *
     * Необходима для пережатия изображения без искажения пропорций.
     */
    fun maxHeight(maxHeight: Int): ImageLoaderInterface

    /**
     * Масштабирование изображения по размеру виджета с обрезкой излишков.
     *
     * @param isCrop флаг активации трансформации
     */
    fun centerCrop(isCrop: Boolean = true): ImageLoaderInterface

    /**
     * Преобразование прямоугольного изображения в круглое.
     *
     * @param isCircle флаг активации трансформации
     */
    fun circle(isCircle: Boolean = true): ImageLoaderInterface

    /**
     * Скругление углов у прямоугольного изображения.
     *
     * @param isRoundedCorners флаг активации трансформации
     * @param radiusPx радиус скругления в px
     * @param marginPx величина отступа в px
     * @param cornerType конфигурация скругляемых углов
     */
    fun roundedCorners(isRoundedCorners: Boolean = true,
                       radiusPx: Int = 0,
                       marginPx: Int = 0,
                       cornerType: CornerType = CornerType.ALL): ImageLoaderInterface

    /**
     * Эффект размытия изображения "Blur".
     *
     * @param isBlur флаг активации трансформации
     * @param blurRadiusPx радиус размытия
     * @param blurDownSampling уровень принудительного понижения качества разрешения изображения
     * @param blurStrategy стратегия размытия изображения
     */
    fun blur(isBlur: Boolean = true,
             blurRadiusPx: Int = 25,
             blurDownSampling: Int = 1,
             blurStrategy: BlurStrategy = BlurStrategy.RENDER_SCRIPT
    ): ImageLoaderInterface

    /**
     * Наложение маски на изображение с поддержкой 9-patch маски.
     *
     * @param isOverlay флаг активации трансформации
     * @param maskResId ссылка на ресурс изображения маски из папки res/drawable
     * @param overlayMode тип оверлея из [PorterDuff.Mode].
     */
    fun mask(isOverlay: Boolean = true, @DrawableRes maskResId: Int, overlayMode: PorterDuff.Mode): ImageLoaderInterface

    /**
     * Применяет указанное значение к размеру
     *
     * @param value множитель
     */
    fun downsamplingMultiplier(@FloatRange(from = 0.0, to = 1.0) value: Float): ImageLoaderInterface

    /**
     * Добавление перехода с растворением между изображениями.
     *
     * @param duration продолжительность перехода (в мс)
     */
    fun crossFade(duration: Int = 300): ImageLoaderInterface

    /**
     * Размножения изображения для соответствия его размеров размерам View
     *
     * @param isTiled должно ли быть изображение размножено
     * @param tileMode тип размножения
     */
    fun tile(isTiled: Boolean = true, tileMode: Shader.TileMode = Shader.TileMode.REPEAT): ImageLoaderInterface

    /**
     * Принудительная вставка изображения во вью.
     * Необходимо в случае, если ссылка на изображение остаётся неизменной, а сама картинка меняется
     */
    fun force(): ImageLoaderInterface

    /**
     * Указание целевой [View].
     *
     * @param view экземпляр [View] для загрузки изображения.
     */
    fun into(view: View)

    /**
     * Загрузка изображения с использованием Listener'ов и указанием целевой [View]
     *
     * @param view экземпляр view, используется для управления жизненным циклом
     * @param onErrorLambda лямбда, вызываемая при ошибке загрузки изображения.
     * @param onCompleteLambda лямбда, вызываемая при успешной загрузке изображения
     * @param onClearMemoryLambda лямбда, вызываемая, когда view может быть очищена. В ней следует
     * производить операции по дополнительному освобождению памяти.
     */
    fun into(
            view: View,
            onErrorLambda: ((errorDrawable: Drawable?) -> Unit)? = null,
            onCompleteLambda: ((resource: Drawable?, imageSource: ImageSource?) -> Unit)?,
            onClearMemoryLambda: ((placeholder: Drawable?) -> Unit)? = null
    )

    /**
     * Получение исходника изображения в формате [Bitmap].
     *
     * Запрос происходит в UI-потоке.
     */
    @WorkerThread
    fun get(): Bitmap?
}