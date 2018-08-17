#App migration
Модуль для миграции приложения. Позволяет выполнять необходиымые действия при изменении версии приложения (обновлении).

#Использование
1. Реализовать наследник класса `AppMigration`, который вмещает в себя всю логику для определенной версии прилолжения
2. Реализовать наследник класса `AppMigrationStorage`, который поставляет экземпляры `AppMigration`
3. Поставить `AppMigrationStorage` в конструктор `AppMigrationManager`
4. Вызвать `AppMigrationManager.tryMigrateApp()` при инициализации приложения 

[Пример использования](https://bitbucket.org/surfstudio/android-standard/src/snapshot-0.3.0/app-migration-sample/)

#Подключение
Gradle:
```
    implementation "ru.surfstudio.android:app-migration:X.X.X"
```