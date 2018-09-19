[Главная страница репозитория](../docs/main.md)

[TOC]

# Custom view
Модуль с реализаций различных полезных кастомных виджетов.

## Список виджетов

+ `TitleSubtitleView` - виджет для отображения текста с подписью;
+ `StandardPlaceHolderView` - стандартный полноэкранный плейсхолдер с поддержкой смены состояний.

## Использование
[Пример использования](../custom-view-sample)

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
[`StandardPlaceHolderView`](../custom-view/STANDARD-PLACEHOLDER-VIEW-README.md)