# DataList page-count
Основная сущность - [`DataList`][dl] - список для пагинируемых данных через механизм page/count
Имеет методы:
 1. `merge(DataList data)`, позволяющий обьединять 2 блока данных
 2. `int getNextPage()` - возвращает номер след блока данных
 3. `bool canGetMore()` - обозначает, можно ли загрузить еще данные
 1. `#transform()` - для преобразования данных в списке

Может объединять два последующих блока, так и в обратном порядке.

Также имеет специальную [утилиту][util] для разбиения большого запроса на мелкие блоки.

### Использование
[Пример использования в приложении](../network-sample)

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:datalist-page-count:X.X.X"
```

[util]: src/main/java/ru/surfstudio/android/datalistpagecount/util/PaginationableUtil.java
[dl]: src/main/java/ru/surfstudio/android/datalistpagecount/domain/datalist/DataList.java