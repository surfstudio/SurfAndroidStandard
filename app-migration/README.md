#App migration
Модуль для миграции приложения. Позволяет выполнять необходиымые действия при изменении версии приложения (обновлении).

#Использование
1. Реализовать наследник класса `AppMigration`, который вмещает в себя всю логику для определенной версии прилолжения
1. Реализовать наследник класса `AppMigrationStorage`, который поставляет экземпляры `AppMigration`
1. Поставить `AppMigrationStorage` в конструктор `AppMigrationManager`
1. Вызвать `AppMigrationManager.tryMigrateApp()` при инициализации приложения 

#Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru), необходимо, 
чтобы корневой `build.gradle` файл проекта был сконфигурирован так, как описано 
[здесь](https://bitbucket.org/surfstudio/android-standard/overview).
  
Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.standard:app-migration:X.X.X"
```