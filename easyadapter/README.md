#Easy adapter
Развитие этого [проекта](https://github.com/MaksTuev/EasyAdapter).

Адаптер для легкого размещаения сложного контента в RecycleView. 
Основная идея - использование для каждого элемента отдельного ItemController`a отвечающего за его отрисовку и поведение.
Возможно использование для статического и динамически наполняемого контента.

#Использование
1. Создается экземпляр класса `EasyAdapter` дефолтным конструктором и передается в `RecyclerView`
1. Для каждого элемента списка создается отдельный класс соответствующего [контроллера](src/main/java/ru/surfstudio/android/easyadapter/controller)
1. Создается и заполняется `ItemList` парами данные+контроллер
1. Заполненный экземпляр `ItemList` передается в `EasyAdapter` через метод `setItems()`

#Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru), необходимо, 
чтобы корневой `build.gradle` файл проекта был сконфигурирован так, как описано 
[здесь](https://bitbucket.org/surfstudio/android-standard/overview).
  
Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.standard:easy-adapter:X.X.X"
```