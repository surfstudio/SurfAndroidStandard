#Picture provider
Содержат в себе набор классов для а получения изображений с устройства.

#Использование
[Sample проект](../picture-provider-sample/README.md)
##Основные классы
`PicturePermissionChecker` логика проверки и получения разрешений для работы с изображениями
`PictureProvider` логика получения изображений с устройства. Работает через rx.

#Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru), необходимо, 
чтобы корневой `build.gradle` файл проекта был сконфигурирован так, как описано 
[здесь](https://bitbucket.org/surfstudio/android-standard/overview).
  
Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.standard:picture-provider:X.X.X"
```