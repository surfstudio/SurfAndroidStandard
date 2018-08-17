#Animations
Содержит набор анимаций и Coordinator.Behavior

##Анимации
[anim] - часто используемые анимации. Поставляются в виде Util класса и extension functions.
###Доступные анимаци
* crossfadeViews(inView: View, outView: View,)
* fadeOut Сокрытие вью с изменением прозрачности
* fadeIn Появление вью с изменением прозрачности
* pulseAnimation Анимация типа "пульс"   
* newSize Изменение ширины и высоты вью
* slideIn Появление вью с эффектом "слайда" в зависимости от gravity
* slideOut Исчезновение вью с эффектом "слайда" в зависимости от gravity

###Длительности анимаций
Значения взяты из [гайда](https://material.io/guidelines/motion/duration-easing.html#duration-easing-dynamic-durations)
* ANIM_ENTERING = 225мс - появление элемента, открытие списка и т.п.
* ANIM_LEAVING = 195мс - сокрытие элемента, закрытие списка и т.п.
* ANIM_TRANSITION = 300мс - стандартные переходы, например, изменение параметров объекта
* ANIM_LARGE_TRANSITION = 375мс - большие измения, например, анимация перехода экранов
 
###Behaviors
[behaviors] - часто используемые Coordinator.Behavior
###Доступные behaviors
* ViewSnackbarBehavior - сдвигает элемент вверх при появлении снекбара
* BottomButtonBehavior - скрывает кнопку при скролле

[anim]: /src/main/java/ru/surfstudio/android/animations/anim
[behaviors]: /src/main/java/ru/surfstudio/android/animations/behaviors

#Использование
[Пример использования](https://bitbucket.org/surfstudio/android-standard/src/snapshot-0.3.0/animations-sample/)

#Подключение
Gradle:
```
    implementation "ru.surfstudio.android:animations:X.X.X"
```