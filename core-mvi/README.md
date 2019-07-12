[Главная страница репозитория](../docs/main.md)

[TOC]

# Core MVI
Является развитием идей [core-mvp](../core-mvp/) и [core-mvp-binding](../core-mvp-binding). 
Так же, этот подход во многом черпал вдохновение из [Redux](https://redux.js.org/), [Flux](https://ru.wikipedia.org/wiki/Flux-%D0%B0%D1%80%D1%85%D0%B8%D1%82%D0%B5%D0%BA%D1%82%D1%83%D1%80%D0%B0) и, конечно, [MVI](http://hannesdorfmann.com/android/model-view-intent).  
## Общее описание
Подход заключается в полном абстрагировании сущностей UI-слоя друг от друга, и в выстраивании непрямых связей между ними на основе отсылки событий. Таким образом, получается решить проблему излишней запутанности связей между элементами и наладить полностью однонаправленный поток.
## Ответственности классов 
TODO

# Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru)
необходимо, чтобы корневой `build.gradle` файл проекта был сконфигурирован так,
как описано [здесь](https://bitbucket.org/surfstudio/android-standard/overview).

Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.android:core-mvi:X.X.X"
```
