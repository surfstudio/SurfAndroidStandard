# Dagger scopes

Основные скоупы приложения для работы с [dagger](https://github.com/google/dagger).

Есть 3 основных вида Scope (области видимости):

* Scope приложения (аннотация [@PerApplication](src/main/java/ru/surfstudio/android/dagger/scope/PerApplication.java))
* Scope активити (аннотация [@PerActivity](src/main/java/ru/surfstudio/android/dagger/scope/PerActivity.java))
* Scope экрана (аннотация [@PerScreen](src/main/java/ru/surfstudio/android/dagger/scope/PerScreen.java))

Эти скоупы используются в приложениях студии. Описание работы [здесь][../docs/common/di.md].

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:dagger-scope:X.X.X"
```

[configurator]: ../core-ui/README.md