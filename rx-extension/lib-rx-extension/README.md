# Rx extension
Утилитарный модуль для работы с Rx.

Содержит:
1. варианты `Action` `Consumer` `Function` без `throws Exception`
2. SchedulerProvider
3. Разные утилиты

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:rx-extension:X.X.X"
```

# Proguard rules
```
-keep class ru.surfstudio.android.rx.extension.ConsumerSafe { *; }
-keep class ru.surfstudio.android.rx.extension.ActionSafe { *; }
```