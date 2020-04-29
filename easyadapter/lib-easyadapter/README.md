# EasyAdapter
Является развитием проекта [EasyAdapter](https://github.com/MaksTuev/EasyAdapter).

Адаптер для легкого размещения сложного контента в RecyclerView. 
Основная идея - использование для каждого элемента отдельного ItemController`a отвечающего за его отрисовку и поведение.
Возможно использование для статического и динамически наполняемого контента.

# Использование
1. Создается экземпляр класса `EasyAdapter` дефолтным конструктором и передается в `RecyclerView`
2. Для каждого элемента списка создается отдельный класс соответствующего [контроллера](src/main/java/ru/surfstudio/android/easyadapter/controller)
3. Создается и заполняется `ItemList` парами данные+контроллер
4. Заполненный экземпляр `ItemList` передается в `EasyAdapter` через метод `setItems()`

[Пример использования](../sample)

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:easyadapter:X.X.X"
```

# Что нового
### Версия EasyAdapter 1.1.0-alpha

Для устранения возможных коллизий и достижения большей гибкости тип
`hash` и `id` был заменен на `Object`.

Если список содержит большое количество элементов и вероятность
возникновения коллизий высока, то следует переопределить метод
контроллера `getItemHash` и вернуть сам объект для избежания коллизий:

```
override fun getItemHash(data: SampleData): Object {
    return data
}
```

В обычных случаях метод `getItemHash` переопределять не надо.

### Версия AndroidStandard 0.3.0

Для устранения возможных коллизий тип ```hash``` и ```id``` был заменен на ```String```.

Если список содержит большое количество элементов и вероятность возникновения коллизий высока,
то следует переопределить метод контроллера ```getItemHash``` и реализовать хеширование объекта,
не используя стандартный метод ```hashCode()```, а использовав библиотеку [guava](https://github.com/google/guava).

Например, хеширование может быть реализовано следующим образом:
```
override fun getItemHash(data: SampleData?): String {
    return Hashing.md5().newHasher()
           .putLong(data.longValue)
           .putString(data.StringValue, Charsets.UTF_8)
           .hash()
           .toString()
}
```

В обычных случаях метод ```getItemHash``` переопределять не надо.

[Описание хеширования с использованием guava](https://github.com/google/guava/wiki/HashingExplained)