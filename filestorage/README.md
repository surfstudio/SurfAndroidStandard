#File storage
Позволяет сохранять и удалять объекты в файлах на устройстве на внутренней или внешней памяти.
Служит для реализации кэша.

#Использование
Наследованием от `BaseLocalCache` или `BaseTextLocalCache`

#Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru), необходимо, 
чтобы корневой `build.gradle` файл проекта был сконфигурирован так, как описано 
[здесь](https://bitbucket.org/surfstudio/android-standard/overview).
  
Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.standard:filestorage:X.X.X"
```