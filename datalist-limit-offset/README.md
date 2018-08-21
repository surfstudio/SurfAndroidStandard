#Datalist limit offset
Основная сущность - DataList - список для пагинируемых данных через механизм limit/offset
Имеет методы:
 1. merge(DataList data), позволяющий обьединять 2 блока данных
 2. int getNextOffset() - возвращает смещение для след блока данных
 3. bool canGetMore() - обозначает, можно ли загрузить еще данные

#Использование
Используется так же, как и [datalist-page-count](../datalist-page-count).

Пример использования [datalist-page-count](../datalist-page-count)
можно найти [здесь](../network-sample).

#Подключение
Gradle:
```
    implementation "ru.surfstudio.android:datalist-limit-offset:X.X.X"
```