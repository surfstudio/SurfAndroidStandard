#Datalist page count
Основная сущность - DataList - список для пагинируемых данных через механизм page/count
Имеет методы:
 1. merge(DataList data), позволяющий обьединять 2 блока данных
 2. int getNextPage() - возвращает номер след блока данных
 3. bool canGetMore() - обозначает, можно ли загрузить еще данные

#Использование
[Пример использования в приложении](../network-sample)

#Подключение
Gradle:
```
    implementation "ru.surfstudio.android:datalist-page-count:X.X.X"
```