[Главная страница репозитория](/docs/main.md)

# Animations

- [Animations](#animations)
    - [Анимации](#анимации)
      - [Доступные анимации](#доступные-анимации)
      - [Длительности анимаций](#длительности-анимаций)
      - [Behaviors](#behaviors)
      - [Доступные behaviors](#доступные-behaviors)
- [Использование](#использование)
- [Подключение](#подключение)

Содержит набор анимаций и Coordinator.Behavior

## Анимации
[anim][anim] - часто используемые анимации. Поставляются в виде Util класса
и *extension functions*.

### Доступные анимации
* `crossfadeViews(inView: View, outView: View)`
* `fadeOut` -  Сокрытие вью с изменением прозрачности
* `fadeIn` -  Появление вью с изменением прозрачности
* `pulseAnimation` - Анимация типа "пульс"
* `newSize` - Изменение ширины и высоты вью
* `slideIn` - Появление вью с эффектом "слайда" в зависимости от gravity
* `slideOut` - Исчезновение вью с эффектом "слайда" в зависимости от gravity

### Длительности анимаций
Значения взяты из [гайда](https://material.io/guidelines/motion/duration-easing.html#duration-easing-dynamic-durations)
* ANIM_ENTERING = 225мс - появление элемента, открытие списка и т.п.
* ANIM_LEAVING = 195мс - сокрытие элемента, закрытие списка и т.п.
* ANIM_TRANSITION = 300мс - стандартные переходы, например, изменение параметров
объекта
* ANIM_LARGE_TRANSITION = 375мс - большие измения, например, анимация перехода
экранов
 
### Behaviors
[behaviors][behaviors] - часто используемые Coordinator.Behavior

### Доступные behaviors
* [`ViewSnackbarBehavior`][vsb] - сдвигает элемент вверх при появлении снекбара
* [`BottomButtonBehavior`][bbb] - скрывает кнопку при скролле

# Использование
[Пример использования](../analytics/sample)

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:animations:X.X.X"
```


[anim]: lib-animations/src/main/java/ru/surfstudio/android/animations/anim
[behaviors]: lib-animations/src/main/java/ru/surfstudio/android/animations/behaviors
[vsb]: lib-animations/src/main/java/ru/surfstudio/android/animations/behaviors/ViewSnackbarBehavior.kt
[bbb]: lib-animations/src/main/java/ru/surfstudio/android/animations/behaviors/BottomButtonBehavior.kt
