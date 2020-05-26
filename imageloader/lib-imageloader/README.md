[Главная страница репозитория](/docs/main.md)

# Image Loader

- [ОТВЕТСТВЕННОЕ ЛИЦО](#ответственное-лицо)
- [ЧТО НОВОГО](#что-нового)
- [ПОДКЛЮЧЕНИЕ](#подключение)
- [ЗАВИСИМОСТИ И ТЕХНОЛОГИИ](#зависимости-и-технологии)
- [ИСПОЛЬЗОВАНИЕ](#использование)
- [ДОКУМЕНТАЦИЯ](#документация)
  - [with](#with)
  - [url](#url)
  - [preview](#preview)
  - [error](#error)
  - [listener](#listener)
  - [errorListener](#errorlistener)
  - [skipCache](#skipcache)
  - [maxWidth](#maxwidth)
  - [maxHeight](#maxheight)
  - [centerCrop](#centercrop)
  - [circle](#circle)
  - [roundedCorners](#roundedcorners)
  - [blur](#blur)
  - [mask](#mask)
  - [downsamplingMultiplier](#downsamplingmultiplier)
  - [crossFade](#crossfade)
  - [into](#into)
  - [get](#get)
  - [force](#force)

Модуль с утилитой для загрузки изображений.

`ImageLoader` - это обёртка над библиотекой-загрузчиком изображений, которая служит для упрощения 
синтаксиса, а также инкапсулирет различные операции над изображениями, которые часто требуется
реализовывать в проектах.

## ОТВЕТСТВЕННОЕ ЛИЦО

**Евгений Сатуров** (saturov@surfstudio.ru)

## ЧТО НОВОГО

* Поддержка даунсэмплинга изображений (#downsamplingMultiplier())
* Поддержка версии Glide 4.7.1;
* Оптимизация производительности при ошибке загрузки изображения.
* Поддержка svg-формата (смотри imageloader-sample)

## ПОДКЛЮЧЕНИЕ

Gradle:

    implementation "ru.surfstudio.android:imageloader:X.X.X"

## ЗАВИСИМОСТИ И ТЕХНОЛОГИИ

Модуль имеет прямые зависимости от следующих модулей `Core`:

    :logger             Модуль для логирования в LogCat и на сервер
    :util-ktx           Модуль с полезными extension-функциями
    
Модуль имеет прямые зависимости от следующих сторонних библиотек:
    
    com.github.bumptech.glide:glide:4.7.1    Загрузчик изображений Glide

Модуль написан на `Kotlin`. В основе модуля - библиотека `Glide`.

## ИСПОЛЬЗОВАНИЕ

Загрузку изображения во `View` обеспечивает фабрика, позволяющая гибко настроить весь процесс загрузки,
политику обработки возникающих в ходе загрузки изображения ошибок, а также постобработку изображения 
при необходимости.

В простейшем случае загрузка изображения при помощи `ImageLoader` будет выглядеть так:

    ImageLoader.with(context)
               .url(IMAGE_URL)
               .into(imageView)

[Пример использования](../sample)

## ДОКУМЕНТАЦИЯ

ImageLoader предоставляет сигнатуру, максимально приближенную к сигнатуре `Glide`, но с некоторыми 
отличиями.

### with
* with(context: Context)

Предоставялет проинициализированный экземпляр `ImageLoader`, готовый к конфиурированию.

### url
* url(url: String)
* url(@DrawableRes drawableResId: Int)
    
Указание ссылки на изображение, которое требуется загрузить. На данный момент реализована поддержка 
загрузки изображения по URL из сети или из ресурсов через указание ссылки формата 
`R.drawable.$image_name`. URL валидируется.

### preview
* preview(@DrawableRes drawableResId: Int)

Указание ссылки на изображение, которое будет выступать в роли плейсхолдера. Оно будет отображаться 
во `View` до тех пор, пока не случится загрузка изображения, либо до тех пор, пока в процессе его 
загрузки произойдёт ошибка. На данный момент реализована загрузка заглушки только из ресурсов через
указание ссылки формата `R.drawable.$image_preview_name`. К изображению заглушки применяются все 
трансформации, применяемые к основному изображению.

### error
* error(@DrawableRes drawableResId: Int)

Указание ссылки на изображение, которое будет выступать в роли заглушки при ошибке. Оно будет 
отображаться во `View` в случае возникновения ошибки в процессе загрузки изображения, указанного в 
методе #url(). Поддерживается загрузка заглушки только из ресурсов через указание ссылки формата 
`R.drawable.$image_error_name`. К изображению заглушки применяются все трансформации, применяемые 
к основному изображению.

### listener
* listener(lambda: ((drawable: Drawable) -> (Unit)))

Этот метод принимает лямбду, возвращающую `Drawable` в случае успешной загрузки изображения. Можно 
использовать как обработчик события успешной загрузки изображения.
 
### errorListener
* errorListener(lambda: ((throwable: Throwable) -> (Unit)))

Этот метод принимает лямбду, возвращаующую `Throwable` в случае возникновения ошибки при загрузке 
изображения. Можно использовать как обработчик события неуспешной загрузки изображения.

### skipCache
* skipCache(skipCache: Boolean)

Настройка политики кэширования. 

    true - приоритет отдаётся закэшированному изображению при наличии;
    false - кэш не используется, изображение всегда запрашивается с сервера
    
### maxWidth
* maxWidth(maxWidth: Int)

Указание максимальной ширины изображения в px. Необходима для пережатия изображения без искажения 
пропорций.

### maxHeight
* maxHeight(maxHeight: Int)

Указание максимальной высоты изображения в px. Необходима для пережатия изображения без искажения 
пропорций.

### centerCrop
* centerCrop(isCrop: Boolean)

Масштабирование изображения по размеру виджета с обрезкой излишков. При использовании `ImageLoader`
следует использовать эту трансформацию, а не `ImageView#scaleType`.

### circle
* circle(isCircle: Boolean)

Преобразование прямоугольного изображения в круглое.

### roundedCorners

* roundedCorners(isRoundedCorners: Boolean, radiusPx: Int, marginPx: Int, cornerType: CornerType)

Скругление углов у прямоугольного изображения. Реализована настройка радиуса скругления, а также 
величина отступа от краёв изображения. Кроме того, реализована возможность скруглять только 
некоторые углы изображения через явное задание параметра `CornerType`.

Возможные параметры конфигурации `CornerType`:

    ALL                                     - все углы;
    TOP, TOP_LEFT, TOP_RIGHT                - верхние углы (все или один из них);
    BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT       - нижние углы (все или один из них);
    LEFT, RIGHT                             - верхний и нижний угол с одной стороны; 
    EXCEPT_TOP_LEFT, EXCEPT_TOP_RIGHT, 
    EXCEPT_BOTTOM_LEFT, EXCEPT_BOTTOM_RIGHT - все углы кроме указанного;
    DIAGONAL_TOP_LEFT, DIAGONAL_TOP_RIGHT   - два угла по диагонали друг от друга, начиная с указанного.
    
### blur
* blur(isBlur: Boolean, blurRadiusPx: Int, blurDownSampling: Int)

Эффект размытия изображения "Blur" с возможностью настройки радиуса размытия и уровня даунсемплинга
(принудительного понижения качества изображения).

### mask
* mask(isOverlay: Boolean, @DrawableRes maskResId: Int)

Маскирование изображения по контуру указанной маски. Поддерживается загрузка маски только из 
ресурсов через указание ссылки формата `R.drawable.$image_mask_name`. Также реализована поддержка 
маскировочных изображений в формате 9-patch.

### downsamplingMultiplier
*  downsamplingMultiplier(@FloatRange(from = 0.0, to = 1.0) value: Float)

Даунсэмплинг изображения. Применяется для сжатия больших изображений во избежание переполнения памяти на устройстве
В качестве параметра метода передаётся занчение множителя сжатия в диапазоне от 0 до 1, где 0 - это максимальная степень сжатия,
а 1 - минимальная.

### crossFade
* crossFade(duration: Int = 300)

Плавный переход с растворением при загрузке изображения с настраиваемой продолжительностью перехода.

### into
* into(view: View)
* into(simpleTarget: SimpleTarget<Bitmap>)
* into(view: View, 
&nbsp;&nbsp;&nbsp;&nbsp;onErrorLambda: ((errorDrawable: Drawable?) -> Unit)?, 
&nbsp;&nbsp;&nbsp;&nbsp;onCompleteLambda: ((resource: Drawable?, imageSource: ImageSource?) -> Unit)?, 
&nbsp;&nbsp;&nbsp;&nbsp;onClearMemoryLambda: ((placeholder: Drawable?) -> Unit)?)
* into(view: View, 
&nbsp;&nbsp;&nbsp;&nbsp;onErrorLambda: ((errorDrawable: Drawable?) -> Unit)?, 
&nbsp;&nbsp;&nbsp;&nbsp;onCompleteLambda: ((resource: Drawable, transition: Transition<in Drawable>?, imageSource: ImageSource?) -> Unit)?, 
&nbsp;&nbsp;&nbsp;&nbsp;onClearMemoryLambda: ((placeholder: Drawable?) -> Unit)?)

Данный метод стартует загрузку изображения через `ImageLoader`. Он имеет две версии:

1. Для указания целевой `View` - это может быть как `ImageView`, так и любая другая `View`, способная 
отображать `background`;
2. Для указания SimpleTarget - используется в случае, когда загруженное изображение по какой-то 
причине не требуется отображать на UI, но необходимо получить его экземпляр.
Для преобразования в `Bitmap` при реализации методов `SimpleTarget` можно использовать `Drawable.toBitmap()`.
3-4. Предоставляет возможность обращаться к коллбекам Target у Glide.
Коллбеки: `onErrorLambda` - вызывается при ошибке загрузки ресурса, `onCompleteLambda` - вызывается 
при успешной загрузке ресурса, `onClearMemoryLambda` - вызывается, когда view может быть очищена. 
В ней следует производить операции по дополнительному освобождению памяти.
В случае, если необходимо работать с `Animatable` drawable (например с `GifDrawable`), в качестве 
`View` должна быть передана `ImageView`.

### get
* get()

Этот метод является синхронной альтернативой перегруженной версии 
`#into(simpleTarget: SimpleTarget<Bitmap>)`.

### force
* force()

Принудительная вставка изображения. Необходимо использовать, если ссылка на изображение остаётся незменной, а сама картинка меняется.