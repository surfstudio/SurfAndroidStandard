[Главная страница репозитория](../docs/main.md)

[TOC]

# Recycler extension

Дополнения для работы с `RecycleView` и `EasyAdapter`

1. [`View-карусель`][carousel], основанная на [easy adapter](../easyadapter/README.md)
2. [`StickyEasyAdapter`][sticky], для работы со sticky header
3. [`SnapHelpers`][snap]
4. [`Dividers`][divider]

# Использование
[Пример использования](../recycler-extension-sample)

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:recycler-extensions:X.X.X"
```


[carousel]: src/main/java/ru/surfstudio/android/recycler/extension/CarouselView.kt
[sticky]: src/main/java/ru/surfstudio/android/recycler/extension/sticky/StickyEasyAdapter.kt
[snap]: src/main/java/ru/surfstudio/android/recycler/extension/snaphelper
[divider]: src/main/java/ru/surfstudio/android/recycler/extension/divider/DividerItemDecoration.java