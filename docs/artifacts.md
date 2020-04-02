[TOC]

# Список артефактов

1. [core-ui](../core-ui/README.md) -
модуль расширяющий возможности ui части Android Framework. Может быть использован для
создания базовых классов, необходимых для реализации паттернов MVP, MVVM
1. [core-mvp](../core-mvp/README.md) -
расширение core-ui для гибридной архитектуры MVP + Presentation Model.
1. [core-mvp-binding](../core-mvp-binding/README.md) -
модуль с поддержкой DataBinding.
1. [core-app](../core-app/README.md) -
конфигурирует контекст приложения.
1. [mvp-dialog](../mvp-dialog/README.md) -
расширение модуля core-mvp для работы с диалогами.
1. [mvp-widget](../mvp-widget/README.md) -
Расширение модуля core-mvp для работы со view(в терминах android фреймвока)
как со View(в терминах MVP).
1. [easyadapter](../easyadapter/README.md) -
адаптер для легкого размещаения сложного контента в RecyclerView.
1. [analytics](../analytics/README.md) -
модуль для фиксирования событий в приложении
1. [firebase-analytics](../firebase-analytics/README.md) -
реализация аналитики с использованием Firebase
1. [network](../network/README.md) -
модуль для быстрого конфигурирования работы с сетью.
1. [filestorage](../filestorage/README.md) -
модуль для построения кеша на основе файлов.
1. [push](../push/README.md) -
модуль для работы с push-сообщениями от сервера
1. [dagger-scope](../dagger-scope/README.md) -
модуль с набором скоупов для DI dagger
1. [logger](../logger/README.md) -
модуль для логирования в logcat и на сервер
1. [converter-gson](../converter-gson/README.md) -
модуль для парсинга json ответов сервера
1. [imageloader](../imageloader/README.md) -
модуль с загрузчиком изображений
1. [animations](../animations/README.md) -
модуль c анимациями и Coordinator.Behavior
1. [picture-provider](../picture-provider/README.md) -
модуль для получения изображения с устройства
1. [camera-view](https://bitbucket.org/surfstudio/android-camera-view/src/master/camera-view/) -
вью отображающая изображение с камеры
1. [app-migration](../app-migration/README.md) -
модуль для миграции приложения на новую версию
1. [connection](../connection/README.md) -
получение состояния сети.
1. [custom-view](../custom-view/README.md) -
набор кастомных вью
1. [datalist-limit-offset](../datalist-limit-offset/README.md) -
работа с пагинацией через limit/offset
1. [datalist-page-count](../datalist-page-count/README.md) -
работа с пагинацией через page/count
1. [recycler-extension](../recycler-extension/README.md) -
дополнения для работы с `RecyclerView` и `EasyAdapter`
1. [rx-extension](../rx-extension/README.md) -
утилитарный модуль для работы с Rx.
1. [shared-pref](../shared-pref/README.md) -
утилитарный модуль для работы с `SharedPreferences`
1. [util-ktx](../util-ktx/README.md) -
модуль c утилитарными классами для android фреймворка и языков.
1. [standard-dialog](../standard-dialog/README.md) -
модуль c простым да/нет диалогом, в который можно передать строковые ресурсы или сами строки.
1. [message-controller](../message-controller/README.md) -
модуль для отображения `Snackbar`.
1. [broadcast-extension](../broadcast-extension/README.md) -
модуль, расширяющий `BroadcastReceiver`.
1. [location](../location/README.md) -
модуль для работы с местоположением.
1. [toolbar_config](../toolbar-config/README.md) -
модуль для гибкой и удобной настройки Toolbar'а

Дополнительно:

1. [template](../template/README.md) -
модуль для инициализации нового приложения.
1. [templates](../templates/README.md) -
Live и File Templates для AndroidStudio
1. [sample-common](../sample-common/README.md) -
модуль, содержащий общие ресурсы для примеров к другим модулям.
1. [sample-dagger](../sample-dagger/README.md) -
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
implementation "ru.surfstudio.android:toolbar-config:${version}"
```