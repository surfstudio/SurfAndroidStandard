[Главная](../../main.md)

[TOC]

# Core mvp binding
**(Данный модуль является экспериментальным и не является обязательным
стандартом использования в проекте)**
Поддежка data-binding

Основные классы:
* [`BindData`][bd]
* [`BindsHolder`][bh]
* [`BaseBindableView`][bbv]
* [`BaseBindingPresenter`][bbp]

# Использование
[Пример использования](../core-mvp-binding-sample)

# Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru)
необходимо, чтобы корневой `build.gradle` файл проекта был сконфигурирован так,
как описано [здесь](https://bitbucket.org/surfstudio/android-standard/overview).
  
Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.android:core-mvp-binding:X.X.X"
```

[bd]: src/main/java/ru/surfstudio/android/core/mvp/binding/BindData.kt
[bh]: src/main/java/ru/surfstudio/android/core/mvp/binding/BindsHolder.kt
[bbv]: src/main/java/ru/surfstudio/android/core/mvp/binding/BaseBindableView.kt
[bbp]: src/main/java/ru/surfstudio/android/core/mvp/binding/BaseBindingPresenter.kt