[Главная страница репозитория](/docs/main.md)

# Custom view
Модуль с реализаций различных полезных кастомных виджетов.

## Список виджетов

+ `TitleSubtitleView` - виджет для отображения текста с подписью;
+ `ShadowLayout` - контейнер для отрисовки красивой тени под содержащимися внутри элементами.

## Использование
[Пример использования](sample)

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