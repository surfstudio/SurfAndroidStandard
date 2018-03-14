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

#Деплой в репозиторий артефактов
1. Поднимаем moduleVersionCode и moduleVersionName в файле config.gradle 
3. Ставим тег с соответствующей версией
3. Выполняем ``` ./gradlew clean uploadArchives ```
3. ...
4. Profit

# Импорт атефактов
## build.gradle(app)
```
implementation "ru.surfstudio.standard:core-app:${version}"
implementation "ru.surfstudio.standard:core-mvp:${version}"
implementation "ru.surfstudio.standard:core-ui:${version}"
implementation "ru.surfstudio.standard:analytics:${version}"
implementation "ru.surfstudio.standard:firebase-analytics:${version}"
implementation "ru.surfstudio.standard:network:${version}"
implementation "ru.surfstudio.standard:filestorage:${version}"
implementation "ru.surfstudio.standard:push:${version}"
implementation "ru.surfstudio.standard:dagger-scope:${version}"
implementation "ru.surfstudio.standard:logger:${version}"
implementation "ru.surfstudio.standard:converter-gson:${version}"
implementation "ru.surfstudio.standard:easyadapter:${version}"
implementation "ru.surfstudio.standard:easyadapter-carousel:${version}"
implementation "ru.surfstudio.standard:imageloader:${version}"
implementation "ru.surfstudio.standard:animations:${version}"
implementation "ru.surfstudio.standard:picture-provider:${version}"
implementation "ru.surfstudio.standard:camera-view:${version}"
implementation "ru.surfstudio.standard:app-migration:${version}"
implementation "ru.surfstudio.standard:connection:${version}"
implementation "ru.surfstudio.standard:custom-view:${version}"
implementation "ru.surfstudio.standard:datalist-limit-offset:${version}"
implementation "ru.surfstudio.standard:datalist-page-count:${version}"
implementation "ru.surfstudio.standard:mvp-dialog:${version}"
implementation "ru.surfstudio.standard:mvp-widget:${version}"
implementation "ru.surfstudio.standard:recycle-extension:${version}"
implementation "ru.surfstudio.standard:rx-extension:${version}"
implementation "ru.surfstudio.standard:shared-pref:${version}"
implementation "ru.surfstudio.standard:util-ktx:${version}"
implementation "ru.surfstudio.standard:template:${version}"

```
## build.gradle(root)
```       
maven {
	url "${surf_maven_libs_url}"
    credentials {
    username = "${surf_maven_username}"
    password = "${surf_maven_password}"
    }
}
```
## gradle.properties
```properties
surf_maven_libs_url = http://artifactory.surfstudio.ru/artifactory/libs-release-local
surf_maven_username = build
surf_maven_password = AP5oyEgS8WyzJ37itfGvKvUSxdgFA8KGvyM9WJ
```
#Работа со snapshot
Если разработка приложения идет параллельно с разработкой одного или нескольких модулей стоит задуматься об обозначении версии артефакта как [SNAPSHOT](https://maven.apache.org/guides/getting-started/index.html#What_is_a_SNAPSHOT_version) 
Это позволит оперативно вносить изменения без обновления версии артефакта в приложении каждым разработчиком.
##Правила работы
1. Версия snapshot-артефакта обозначается как ```{version}-SNAPSHOT```. Например: *0.1.2-SNAPSHOT*
1. Перед началом изменений в модулях, стоит поднять версию по [правилам](https://semver.org/)
1. После завершения работ по изменению модулей, необходимо зафиксировать текущую версию артефатов, [задеплоив](#деплой-в-репозиторий-артефактов) версию без суффикса -SNAPSHOT
1. Для оперативного обновления нужно добавить в *build.gradle(root)*
```
configurations.all {
    resolutionStrategy.cacheDynamicVersionsFor 10, 'seconds'
    resolutionStrategy.cacheChangingModulesFor 10, 'seconds'
}
```