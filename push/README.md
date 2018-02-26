#Push
Содержит базовые классы для получения пушей от сервера с последующей обработкой. 
Возможно определение поведения для различных типов сообщений.

Для встраивания в проект необходимо сделать следующие действия:
1. Проинициализировать [NotificationCenter](/home/azaytsev/StudioProjects/android-standard/push/src/main/java/ru/surfstudio/android/notification/NotificationCenter.kt)
   с помощью метода configure(), вызвав внутри методы setActiveActivityHolder, setPushHandleStrategyFactory
1. Добавить маркерный интерфейс [PushHandlingActivity](/home/azaytsev/StudioProjects/android-standard/push/src/main/java/ru/surfstudio/android/notification/ui/notification/PushHandlingActivity.kt)
   к активити-лаунчер (или другой, с которой будет происходить навигация*)
1. В DefaultActivityLifecycleCallbacks добавить обработку старта активити методом 
   NotificationCenter.onActivityStarted()
1. Добавить объект, наследующий [AbstractPushHandleStrategyFactory](/home/azaytsev/StudioProjects/android-standard/push/src/main/java/ru/surfstudio/android/notification/ui/notification/AbstractPushHandleStrategyFactory.kt),
   в котором переопределить map c соотвествием типа пуша стратегии его обработки
1. Добавить firebase в проект, в методе onMessageReceived обработать сообщение
   с помощью NotificationCenter.onMessageReceived()