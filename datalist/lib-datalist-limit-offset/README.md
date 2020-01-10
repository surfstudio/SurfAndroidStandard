# Datalist limit-offset
Основная сущность - [`DataList`][dl] - список для пагинируемых данных через механизм limit/offset
Имеет методы:
 1. `merge(DataList data)`, позволяющий обьединять 2 блока данных
 2. `int getNextOffset()` - возвращает смещение для след блока данных
 3. `bool canGetMore()` - обозначает, можно ли загрузить еще данные
 1. `#transform()` - для преобразования данных в списке

Может объединять два последующих блока, так и в обратном порядке.

Также имеет специальную [утилиту][util] для разбиения большого запроса на мелкие блоки.

### Использование
Используется так же, как и [datalist-page-count](../datalist-page-count/README.md).

Пример использования [datalist-page-count](../datalist-page-count)
можно найти [здесь](../network-sample).

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:datalist-limit-offset:X.X.X"
```

[util]: src/main/java/ru/surfstudio/android/datalistlimitoffset/util/PaginationableUtil.java
[dl]: src/main/java/ru/surfstudio/android/datalistlimitoffset/domain/datalist/DataList.java