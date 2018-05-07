#App migration
Модуль для миграции приложения. Позволяет выполнять необходиымые действия при изменении версии приложения (обновлении).

#Использование
1. Реализовать наследник класса `AppMigration`, который вмещает в себя всю логику для определенной версии прилолжения
1. Реализовать наследник класса `AppMigrationStorage`, который поставляет экземпляры `AppMigration`
1. Поставить `AppMigrationStorage` в конструктор `AppMigrationManager`
1. Вызвать `AppMigrationManager.tryMigrateApp()` при инициализации приложения 

#Подключение
```
    implementation "ru.surfstudio.standard:app-migration:X.X.X"
```