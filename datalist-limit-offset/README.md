#Datalist limit offset
Основная сущность - DataList - список для пагинируемых данных через механизм limit/offset
Имеет методы:
 1. merge(DataList data), позволяющий обьединять 2 блока данных
 2. int getNextOffset() - возвращает смещение для след блока данных
 3. bool canGetMore() - обозначает, можно ли загрузить еще данные

#Использование
Используется так же, как и [datalist-page-count](https://bitbucket.org/surfstudio/android-standard/src/snapshot-0.3.0/datalist-page-count/).

Пример использования [datalist-page-count](https://bitbucket.org/surfstudio/android-standard/src/snapshot-0.3.0/datalist-page-count/)
можно найти [здесь](https://bitbucket.org/surfstudio/android-standard/src/snapshot-0.3.0/network-sample/).

#Подключение
Gradle:
```
    implementation "ru.surfstudio.android:datalist-limit-offset:X.X.X"
```