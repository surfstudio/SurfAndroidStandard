# Activity holder
Модуль содержащий активити холдер
Нужно инициализировать в application класс

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