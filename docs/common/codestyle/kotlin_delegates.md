# Delegates - Kotlin

В Kotlin существует возможность делегировать создание класса другому классу.
Но кроме этого можно делегировать инициализацию свойств в классе некоторому
объекту-делегату.

Для этого существует ряд стандартных делегатов:
* `lazy {}`
* `observable() {}`

**Lazy** предоставляет ленивую инициализацию свойств, т.е. они будут
проинициализированы единожды при первом вызове, тем значением , которое
возвращает  лямбда.
```kotlin
private val locationManager: LocationManager by lazy {
   (appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
}
```

**Observable** - более интересный делегат. Он принимает начальное значение
и лямбду , у которой три параметра - присваиваемое свойство(KProperty),
старое и новое значения.

Удобно использовать в случае, когда надо например сравнить старое и новое
значение и в зависимости от этого сделать те или иные действия, или же,
когда надо сделать “наблюдаемое” свойство(т.е. надо реагировать на его изменения)

```kotlin
private var selectedPinChangeDelegateData: PinSelectedData by Delegates.observable(
    PinSelectedData()
) { _, oldValue, newValue ->
   if (newValue != oldValue) {
       pinSelectedSubject.onNext(newValue)
       renderMarkerSelection(oldValue, newValue)
   }
}
```

Также можно писать свои делегаты. Для этого надо наследоваться от
ReadWriteProperty / ReadOnlyProperty и переопределить их методы.

Задачка по делегатам на KotlinKoans