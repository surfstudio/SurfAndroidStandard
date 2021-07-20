[Главная страница репозитория](/docs/main.md)

# Navigation-observer

Модуль для подписки на результат работы любого типа экрана с механизмами навигации из модуля
[navigation](../lib-navigation).

Основные сущности:

- [ResultRoute<R>][result-route] - маркерный интерфейс для [Route][base-route], который служит для передачи результата работы экрана.
- [ScreenResultObserver][observer] - класс, который позволяет добавить и убрать listener результата
работы экрана.
- [ScreenResultEmitter][emitter] - класс, который передает результат работы экрана observer'у, чтобы
тот уведомил своих подписчиков.
- [ScreenResultBus][bus] - шина передачи результата работы экрана, которая является одновременно и
observer, и emitter. Содержит в себе логику сохранения результата работы экрана, если его не обработал ни один подписчик.
- [ScreenResultStorage][storage] - хранилище необработанных результатов работы экрана.
Может понадобиться, когда, например, процесс приложения убивает система,
восстановливает приложение на последнем экране (из которого необходимо извлечь результат),
и мы теряем подписчика, который в будущем должен обработать результат.
Есть базовая реализация [FileScreenResultStorage][file-storage],
которая сохраняет результаты работы экрана в файловый кеш.
- [EmitScreenResult][command] - команда с результатом работы экрана. Содержит route и результат.
- [AppCommandExecutorWithResult][executor] - исполнитель команд навигации, поддерживающий
[команду EmitScreenResult][command] и передающий результат экрана в screenResultEmitter.

## Использование
[Пример использования без архитектурных подходов](../sample/)

[Пример использования Surf MVP + Dagger](../sample-standard/)

## Подключение

Gradle:
```
    implementation "ru.surfstudio.android:navigation-observer:X.X.X"
```

[observer]: src/main/java/ru/surfstudio/android/navigation/observer/ScreenResultObserver.kt
[emitter]: src/main/java/ru/surfstudio/android/navigation/observer/ScreenResultEmitter.kt
[bus]: src/main/java/ru/surfstudio/android/navigation/observer/bus/ScreenResultBus.kt
[result-route]: src/main/java/ru/surfstudio/android/navigation/observer/route/ResultRoute.kt
[base-route]: ../lib-navigation/src/main/java/ru/surfstudio/android/navigation/route/BaseRoute.kt
[storage]: src/main/java/ru/surfstudio/android/navigation/observer/storage/ScreenResultStorage.kt
[file-storage]: src/main/java/ru/surfstudio/android/navigation/observer/storage/file/FileScreenResultStorage.kt
[executor]: src/main/java/ru/surfstudio/android/navigation/observer/executor/AppCommandExecutorWithResult.kt
[command]: src/main/java/ru/surfstudio/android/navigation/observer/command/EmitScreenResult.kt