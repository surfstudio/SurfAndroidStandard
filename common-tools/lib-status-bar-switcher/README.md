# Status bar switcher

Сущность для автоматического переключения цвета статус-бара в зависимости от цвета контента, находящегося под ним.

# Использование
[Пример использования для определенного экрана](../sample)

Для того, чтобы цвет статус-бара переключался автоматически для каждого экрана, нужно воспользоваться ActivityLifecycleCallbacks:

```kotlin
object : Application.ActivityLifecycleCallbacks() {

    //...
    
    override fun onActivityResumed(activity: Activity) {
        statusBarSwitcher.attach(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        statusBarSwitcher.detach()
    }
}
```