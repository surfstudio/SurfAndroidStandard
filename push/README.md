[Главная](../docs/main.md)

[TOC]

# Push
Содержит базовые классы для получения пушей от сервера с последующей обработкой. 
Возможно определение поведения для различных типов сообщений.

## Использование

1. Сконфигурировать [`PushHandler`][handler], используя, например `DefaultPushHandler` - [пример][nm]
1. В DefaultActivityLifecycleCallbacks добавить обработку старта активити методом
   `pushHandler.onActivityStarted()`
1. Добавить маркерный интерфейс [PushHandlingActivity](src/main/java/ru/surfstudio/android/notification/ui/notification/PushHandlingActivity.kt)
   к активити-лаунчер (или другой, с которой будет происходить навигация)
1. Добавить объект, наследующий [AbstractPushHandleStrategyFactory](src/main/java/ru/surfstudio/android/notification/ui/notification/AbstractPushHandleStrategyFactory.kt),
   в котором переопределить map c соотвествием типа пуша стратегии его обработки
1. Добавить firebase в проект, заинжектить туда `PushHandler`, в методе onMessageReceived обработать сообщение
   с помощью `pushHandler.handleMessage()`
   
[Пример использования](../firebase-sample)

## Release Notes


# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:push:X.X.X"
```

[nm]: ../firebase-sample/src/main/java/ru/surfstudio/android/firebase/sample/app/dagger/NotificationModule.kt
[handler]: ../template/base-ui/src/main/java/ru/surfstudio/standard/base_ui/push/DefaultPushHandler.kt