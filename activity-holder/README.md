# App migration
Модуль для миграции приложения. Позволяет выполнять необходиымые действия при изменении версии приложения (обновлении).

# Использование
1. Реализовать наследник класса [`AppMigration`][am], который вмещает в себя всю логику для определенной версии прилолжения
2. Реализовать наследник класса [`AppMigrationStorage`][ams], который поставляет экземпляры `AppMigration`
3. Поставить `AppMigrationStorage` в конструктор [`AppMigrationManager`][amm]
4. Вызвать `AppMigrationManager.tryMigrateApp()` при инициализации приложения 

[Пример использования](../app-migration-sample)

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:app-migration:X.X.X"
```

[am]: src/main/java/ru/surfstudio/android/app/migration/AppMigration.java
[ams]: src/main/java/ru/surfstudio/android/app/migration/AppMigrationStorage.java
[amm]: src/main/java/ru/surfstudio/android/app/migration/AppMigrationManager.java