# Dagger scopes
В студийных приложениях используем паттерн **Инъекция зависимостей**.

*Инъекция зависимостей* реализована с помощью библиотеки **Dagger 2**.
Область видимости более высокого уровня расширяет область видимости более низкого уровня.
С помощью данного паттерна конфигурируются внешние модули,
поставляются зависимости на внешние слои.

Основные скоупы приложения для работы с [dagger](https://github.com/google/dagger).

Есть 3 основных вида Scope (области видимости):

* Scope приложения (аннотация [@PerApplication](src/main/java/ru/surfstudio/android/dagger/scope/PerApplication.java)) -
предоставляет объекты, слоя Interactor.
Эти объекты являются синглтонами.

* Scope активити (аннотация [@PerActivity](src/main/java/ru/surfstudio/android/dagger/scope/PerActivity.java)) -
предоставляет обьекты, привязанные к жизненному циклу Активити (например RxBus).
Этот Скоуп рамполагается между PerApplication и PerScreen.
Не забывайте пробрасывать обьекты из PerApplication в PerScreen через AppComponent и ActivityComponent.

* Scope экрана (аннотация [@PerScreen](src/main/java/ru/surfstudio/android/dagger/scope/PerScreen.java)) -
предоставляет объекты, привязанные к жизненному циклу экрана.
**@PerScreen** может применяться для экранов, основанных как на Activity, так и на Fragmentб
а также на Widget.

*Замечание*: несмотря на название Activity помечается как `@PerScreen`.

Разрешается добавлять скоупы для собственных нужд, между уровнем приложения и уровнем экрана.

Для конфигурирования Dagger'а на экране создаются специальные [конфигураторы][configurator],
содержащие dagger-компонент и dagger-модули.
Название конфигуратора должно соответствовать следующему правилу: <название экрана>ScreenConfigurator.

Все объекты для удовлетворения зависимостей разделены на логически связанные dagger модули.

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:dagger-scope:X.X.X"
```

[configurator]: ../core-ui/README.md