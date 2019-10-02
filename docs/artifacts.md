[TOC]

# Список артефактов

1. [core-ui](../../HEAD/core-ui/) -
модуль расширяющий возможности ui части Android Framework. Может быть использован для
создания базовых классов, необходимых для реализации паттернов MVP, MVVM
1. [core-mvp](../../HEAD/mvp/lib-core-mvp/) -
расширение core-ui для гибридной архитектуры MVP + Presentation Model.
1. [core-mvp-binding](../../HEAD/core-mvp-binding/) -
модуль с поддержкой DataBinding.
1. [core-app](../../HEAD/deprecated/core-app/) -
конфигурирует контекст приложения.
1. [mvp-dialog](../../HEAD/mvp/lib-mvp-dialog/) -
расширение модуля core-mvp для работы с диалогами.
1. [mvp-widget](../../HEAD/mvp/lib-mvp-widget/) -
Расширение модуля core-mvp для работы со view(в терминах android фреймвока)
как со View(в терминах MVP).
1. [easyadapter](../../HEAD/easyadapter/) -
адаптер для легкого размещения сложного контента в RecyclerView.
1. [analytics](../../HEAD/analytics/lib-analytics/) -
модуль для фиксирования событий в приложении
1. [firebase-analytics](../../HEAD/analytics/lib-firebase-analytics/) -
реализация аналитики с использованием Firebase
1. [network](../../HEAD/deprecated/network/) -
модуль для быстрого конфигурирования работы с сетью.
1. [filestorage](../../HEAD/filestorage/) -
модуль для построения кеша на основе файлов.
1. [push](../../HEAD/push/) -
модуль для работы с push-сообщениями от сервера
1. [dagger-scope](../../HEAD/dagger-scope/) -
модуль с набором скоупов для DI dagger
1. [logger](../../HEAD/logger/) -
модуль для логирования в logcat и на сервер
1. [converter-gson](../../HEAD/deprecated/converter-gson/) -
модуль для парсинга json ответов сервера
1. [imageloader](../../HEAD/imageloader/) -
модуль с загрузчиком изображений
1. [animations](../../HEAD/animations/) -
модуль c анимациями и Coordinator.Behavior
1. [picture-provider](../../HEAD/picture-provider/) -
модуль для получения изображения с устройства
1. [camera-view](https://bitbucket.org/surfstudio/android-camera-view/src/master/camera-view/) -
вью отображающая изображение с камеры
1. [app-migration](../../HEAD/app-migration/) -
модуль для миграции приложения на новую версию
1. [connection](../../HEAD/connection/) -
получение состояния сети.
1. [custom-view](../../HEAD/custom-view/) -
набор кастомных вью
1. [datalist-limit-offset](../../HEAD/datalist/lib-datalist-limit-offset/) -
работа с пагинацией через limit/offset
1. [datalist-page-count](../../HEAD/datalist/lib-datalist-page-count/) -
работа с пагинацией через page/count
1. [recycler-extension](../../HEAD/recycler-extension/) -
дополнения для работы с `RecyclerView` и `EasyAdapter`
1. [rx-extension](../../HEAD/rx-extension/) -
утилитарный модуль для работы с Rx.
1. [shared-pref](../../HEAD/shared-pref/) -
утилитарный модуль для работы с `SharedPreferences`
1. [util-ktx](../../HEAD/util-ktx/) -
модуль c утилитарными классами для android фреймворка и языков.
1. [standard-dialog](../../HEAD/standard-dialog/) -
модуль c простым да/нет диалогом, в который можно передать строковые ресурсы или сами строки.
1. [message-controller](../../HEAD/message-controller/) -
модуль для отображения `Snackbar`.
1. [broadcast-extension](../../HEAD/broadcast-extension/) -
модуль, расширяющий `BroadcastReceiver`.
1. [location](../../HEAD/location/) -
модуль для работы с местоположением.

Дополнительно:

1. [template](../../HEAD/template/) -
модуль для инициализации нового приложения.
1. [android-studio-settings](../../HEAD/android-studio-settings/) -
Live и File Android-studio-settings для AndroidStudio
1. [sample-common](../../2d616c2a00525c4e3bcb412f0e10c342125c6ec7/common/lib-sample-common/) -
модуль, содержащий общие ресурсы для примеров к другим модулям.
1. [sample-dagger](../../2d616c2a00525c4e3bcb412f0e10c342125c6ec7/common/lib-sample-dagger/) -
модуль, содержащий базовую конфигурацию Dagger для примеров к другим модулям.

# Импорт артефактов

### build.gradle(app)

```groovy
implementation "ru.surfstudio.android:core-app:${version}"
implementation "ru.surfstudio.android:core-mvp:${version}"
implementation "ru.surfstudio.android:core-mvp-binding:${version}"
implementation "ru.surfstudio.android:core-ui:${version}"
implementation "ru.surfstudio.android:analytics:${version}"
implementation "ru.surfstudio.android:firebase-analytics:${version}"
implementation "ru.surfstudio.android:network:${version}"
implementation "ru.surfstudio.android:filestorage:${version}"
implementation "ru.surfstudio.android:push:${version}"
implementation "ru.surfstudio.android:dagger-scope:${version}"
implementation "ru.surfstudio.android:logger:${version}"
implementation "ru.surfstudio.android:message-controller:${version}"
implementation "ru.surfstudio.android:converter-gson:${version}"
implementation "ru.surfstudio.android:easyadapter:${version}"
implementation "ru.surfstudio.android:imageloader:${version}"
implementation "ru.surfstudio.android:animations:${version}"
implementation "ru.surfstudio.android:picture-provider:${version}"
implementation "ru.surfstudio.android:app-migration:${version}"
implementation "ru.surfstudio.android:connection:${version}"
implementation "ru.surfstudio.android:custom-view:${version}"
implementation "ru.surfstudio.android:datalist-limit-offset:${version}"
implementation "ru.surfstudio.android:datalist-page-count:${version}"
implementation "ru.surfstudio.android:mvp-dialog:${version}"
implementation "ru.surfstudio.android:mvp-widget:${version}"
implementation "ru.surfstudio.android:recycler-extension:${version}"
implementation "ru.surfstudio.android:rx-extension:${version}"
implementation "ru.surfstudio.android:shared-pref:${version}"
implementation "ru.surfstudio.android:standard-dialog:${version}"
implementation "ru.surfstudio.android:util-ktx:${version}"
implementation "ru.surfstudio.android:broadcast-extension:${version}"
implementation "ru.surfstudio.android:location:${version}"
```
