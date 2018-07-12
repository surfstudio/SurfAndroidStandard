#Datalist limit offset
Основная сущность - DataList - список для пагинируемых данных через механизм limit/offset
Имеет методы:
 1. merge(DataList data), позволяющий обьединять 2 блока данных
 1. int getNextOffset() - возвращает смещение для след блока данных
 1. bool canGetMore() - обозначает, можно ли загрузить еще данные

#Подключение
Gradle:
```
    implementation "ru.surfstudio.android:datalist-limit-offset:X.X.X"
```