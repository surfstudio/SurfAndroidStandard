# Activity holder
Модуль, содержащий `ActivityHolder` - содержит текущую активити.
Необходимо инициализировать в application классе

# Пример использования
```kotlin
object : Application.ActivityLifecycleCallbacks() {

    //...
    
    override fun onActivityResumed(activity: Activity) {
        activeActivityHolder.activity = activity
    }

    override fun onActivityStopped(activity: Activity) {
        activeActivityHolder.clearActivity()
    }
}
```

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:activity-holder:X.X.X"
```