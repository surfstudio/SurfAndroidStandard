[Главная страница репозитория](../../docs/main.md)

# Custom view
Модуль с реализаций различных полезных кастомных виджетов.

## Список виджетов

+ `TitleSubtitleView` - виджет для отображения текста с подписью;
+ `StandardPlaceHolderView` - полноэкранный плейсхолдер с поддержкой смены состояний. **В настоящее время разработка приостоновлена. Следует использовать [PlaceHolderViewContainer](src/main/java/ru/surfstudio/android/custom/view/placeholder/PlaceHolderViewContainer.kt)**
+ `ShadowLayout` - контейнер для отрисовки красивой тени под содержащимися внутри элементами.

## Использование
[Пример использования](../sample)

## Подключение
Gradle:
```
    implementation "ru.surfstudio.android:custom-view:X.X.X"
```    
## Зависимости и технологии

Модуль имеет прямые зависимости от следующих модулей `Core`:

    :util-ktx           Модуль с полезными extension-функциями
    :rx-extension       Модуль для работы с RxJava
    :animations         Модуль с анимациями
    
Модуль имеет прямые зависимости от следующих сторонних библиотек:

    me.zhanghai.android.materialprogressbar:library:$materialProgressBarVersion    Улучшенная версия ProgressBar
    com.wang.avi:library:$wangAviLibraryVersion                                    Кастомные Loader Indicators

## Документация
[`StandardPlaceHolderView`](STANDARD-PLACEHOLDER-VIEW-README.md)