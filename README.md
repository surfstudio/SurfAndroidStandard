#Список артефактов
1. [core-app](core-app/README.md) - базовая часть ядра для построения приложения.  
1. [core-mvp](core-mvp/README.md) - реализации дополненной MVP архитектуры.  
1. [core-ui](core-ui/README.md) - модуль для построения графическго интерфейса  
1. [analytics](analytics/README.md) - модуль для фиксирования событий в приложении
1. [firebase-analytics](firebase-analytics/README.md) - реализация аналитики с использованием firebase
1. [network](network/README.md) - модуль для работы с сетью.
1. [filestorage](filestorage/README.md) - модуль для построения кеша на основе файлов.
1. [push](push/README.md) - модуль для работы с push-сообщениями от сервера
1. [dagger-scope](dagger-scope/README.md) - модуль с набором скоупов для DI dagger
1. [logger](logger/README.md) - модуль для логирования в logcat и на сервер
1. [converter-gson](converter-gson/README.md) - модуль для парсинга json ответов сервера
1. [easyadapter](easyadapter/README.md) - модуль с адаптером для RecycleView 
1. [easyadapter-carousel](easyadapter-carousel/README.md) - модуль c view-каруселью, основанной на easy adapter
1. [imageloader](imageloader/README.md) - модуль с загрузчиком изображений
1. [animations](animations/README.md) - модуль c анимациями и Coordinator.Behavior
1. [picture-provider](picture-provider/README.md) - модуль для получения изображения с устройства
1. [camera-view](camera-view/README.md) - Вью должно быть связано с жизенным циклом активити/фрагмента
1. [app-migration](app-migration/README.md) - Вью должно быть связано с жизенным циклом активити/фрагмента
1. [connection](connection/README.md) - работа с состоянитем сети.
1. [custom-view](custom-view/README.md) - набор кастомных вью
1. [datalist-limit-offset](datalist-limit-offset/README.md) - работа с пагинацией через limit/offset
1. [datalist-page-count](datalist-page-count/README.md) - работа с пагинацией чере page
1. [mvp-dialog](mvp-dialog/README.md) - применение mvp к диалогам, где диалог - mvp-view
1. [mvp-widget](mvp-widget/README.md) - применение mvp к android-view, где android-view - mvp-view
1. [recycle-extension](recycler-extension/README.md) - дополнения для работы с `RecycleView` и `EasyAdapter`
1. [rx-extension](rx-extension/README.md) - утилитарный модуль для работы с rx.
1. [shared-pref](shared-pref/README.md) - утилитарный модуль для работы с `SharedPreferences`
1. [util-ktx](util-ktx/README.md) - модуль c утилитарными классами для android фреймвока и языков.
1. [standard-dialog](standard-dialog/README.md) - модуль c простым да/нет диалогом, в который можно передать строковые ресурсы или сами строки.
1. [template](template/README.md) - модуль для инициализации нового приложения. Не провайдится в репозиторий артефактов

# Импорт атефактов
## build.gradle(app)
```
implementation "ru.surfstudio.android:core-app:${version}"
implementation "ru.surfstudio.android:core-mvp:${version}"
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
implementation "ru.surfstudio.android:camera-view:${version}"
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

```
## build.gradle(root)
```       
maven {
	url "${surf_maven_libs_url}"
}
```
## gradle.properties
```properties
surf_maven_libs_url = http://artifactory.surfstudio.ru/artifactory/libs-release-local
```
#Работа со snapshot
Если разработка приложения идет параллельно с разработкой одного или нескольких модулей стоит задуматься об обозначении версии артефакта как [SNAPSHOT](https://maven.apache.org/guides/getting-started/index.html#What_is_a_SNAPSHOT_version)
Для оперативного обновления нужно добавить в build.gradle(root):
```
configurations.all {
    resolutionStrategy.cacheDynamicVersionsFor 10, 'seconds'
    resolutionStrategy.cacheChangingModulesFor 10, 'seconds'
}
```