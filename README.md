#Список артефактов
1. [core](core/README.md) - основная часть приложения.  
1. [analytics](analytics/README.md) - модуль для фиксирования событий в приложении
1. [network](network/README.md) - модуль для работы с сетью.
1. [filestorage](filestorage/README.md) - модуль для построения кеша на основе фалов.
1. [template](template/README.md) - модуль для инициализации нового приложения. Не провайдится в репозиторий артефактов
1. [push](push/README.md) - модуль для работы с push-сообщениями от сервера
1. [dagger-scope](dagger-scope/README.md) - модуль с набором скоупов для DI dagger
1. [logger](logger/README.md) - модуль для логирования в logcat и на сервер
1. [converter-gson](converter-gson/README.md) - модуль для парсинга json ответов сервера
1. [easyadapter](easyadapter/README.md) - модуль с адаптером для RecycleView 
1. [easyadapter-carousel](easyadapter-carousel/README.md) - модуль c view-каруселью, основанной на easy adapter
1. [imageloader](imageloader/README.md) - модуль с загрузчиком изображений
1. [picture-provider](picture-provider/README.md) - модуль для получения изображения с устройства
1. [camera-view](camera-view/README.md) - Вью должно быть связано с жизенным циклом активити/фрагмента

#Деплой в репозиторий артефактов
1. Поднимаем moduleVersionCode и moduleVersionName в файле config.gradle 
3. Ставим тег с соответствующей версией
3. Выполняем ``` ./gradlew clean uploadArchives ```
3. ...
4. Profit

# Импорт атефактов
## build.gradle(app)
```
implementation "ru.surfstudio.standard:core:${version}"  
implementation "ru.surfstudio.standard:analytics:${version}"
implementation "ru.surfstudio.standard:network:${version}"
implementation "ru.surfstudio.standard:filestorage:${version}"
implementation "ru.surfstudio.standard:template:${version}"
implementation "ru.surfstudio.standard:push:${version}"
implementation "ru.surfstudio.standard:dagger-scope:${version}"
implementation "ru.surfstudio.standard:logger:${version}"
implementation "ru.surfstudio.standard:converter-gson:${version}"
implementation "ru.surfstudio.standard:easyadapter:${version}"
implementation "ru.surfstudio.standard:easyadapter-carousel:${version}"
implementation "ru.surfstudio.standard:imageloader:${version}"
implementation "ru.surfstudio.standard:picture-provider:${version}"
implementation "ru.surfstudio.standard:camera-view:${version}"
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
1. Для автоматического обновления snapshot зависимости должны быть помечены `{changing = true}` Пример: `implementation("ru.surfstudio.standard:core:$surfArtefactoryVersion")  { changing=true }`
1. Для оперативного обновления нужно добавить в *build.gradle(root)*
```
configurations.all {
    resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
}
```