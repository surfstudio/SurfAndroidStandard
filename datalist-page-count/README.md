#Datalist limit offset
Основная сущность - DataList - список для пагинируемых данных через механизм page/count
Имеет методы:
 1. merge(DataList data), позволяющий обьединять 2 блока данных
 1. int getNextPage() - возвращает номер след блока данных
 1. bool canGetMore() - обозначает, можно ли загрузить еще данные

#Подключение
Gradle:
```
    implementation "ru.surfstudio.android:datalist-page-count:X.X.X"
```