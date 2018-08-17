#MVP dialog
Расширение модуля [core-mvp](../core-mvp/README.md) для работы с диалогами.

Предоставляет 2 парадигмы работы с диалогами:

1. Диалог как часть родительского вью, события диалога в этом случае получает презентер родительского вью (см CoreSimpleDialogFragment)
2. Диалог с собственным презентером, родительский презентер в этом случе может получить событие с диалога через RxBus (см CoreDialogFragmentView)

#Использование
[Пример использования](https://bitbucket.org/surfstudio/android-standard/src/snapshot-0.3.0/mvp-dialog-sample/)

#Подключение
Gradle:
```
    implementation "ru.surfstudio.android:mvp-dialog:X.X.X"
```