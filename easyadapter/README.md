#Easy adapter
Является развитие проекта [EasyAdapter](https://github.com/MaksTuev/EasyAdapter).

Адаптер для легкого размещаения сложного контента в RecycleView. 
Основная идея - использование для каждого элемента отдельного ItemController`a отвечающего за его отрисовку и поведение.
Возможно использование для статического и динамически наполняемого контента.

#Использование
1. Создается экземпляр класса `EasyAdapter` дефолтным конструктором и передается в `RecyclerView`
1. Для каждого элемента списка создается отдельный класс соответствующего [контроллера](src/main/java/ru/surfstudio/android/easyadapter/controller)
1. Создается и заполняется `ItemList` парами данные+контроллер
1. Заполненный экземпляр `ItemList` передается в `EasyAdapter` через метод `setItems()`

#Подключение
Gradle:
```
    implementation "ru.surfstudio.android:easy-adapter:X.X.X"
```