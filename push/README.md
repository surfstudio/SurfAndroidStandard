# Push
Содержит базовые классы для получения пушей от сервера с последующей обработкой. 
Возможно определение поведения для различных типов сообщений.

## Использование
Для встраивания в проект необходимо сделать следующие действия:
1. Проинициализировать [NotificationCenter](src/main/java/ru/surfstudio/android/notification/NotificationCenter.kt)
   с помощью метода configure(), вызвав внутри методы setActiveActivityHolder, setPushHandleStrategyFactory
2. Добавить маркерный интерфейс [PushHandlingActivity](src/main/java/ru/surfstudio/android/notification/ui/notification/PushHandlingActivity.kt)
   к активити-лаунчер (или другой, с которой будет происходить навигация*)
3. В DefaultActivityLifecycleCallbacks добавить обработку старта активити методом
   NotificationCenter.onActivityStarted()
4. Добавить объект, наследующий [AbstractPushHandleStrategyFactory](src/main/java/ru/surfstudio/android/notification/ui/notification/AbstractPushHandleStrategyFactory.kt),
   в котором переопределить map c соотвествием типа пуша стратегии его обработки
5. Добавить firebase в проект, в методе onMessageReceived обработать сообщение
   с помощью NotificationCenter.onMessageReceived()
   
[Пример использования](../firebase-sample)

#Подключение
Gradle:
```
    implementation "ru.surfstudio.android:push:X.X.X"
```