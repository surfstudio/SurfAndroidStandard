# Util-ktx
Модуль c утилитарными классами.

Можно разделить на:
+ Расширения для языка kotlin
+ Расширения для языка java
+ Расширения для работы с Android фреймвоком
+ Обертки для данных

### Blockable
Сущность, которая может блокировать своё состояние.
Пример: задизейблить какую-либо ячейку в списке

###Checkable
Множественное выделение в списке (аналог checkbox)

### Deletable
Удаляемый элемент списка, который можно потом вернуть
Т.е. в самом списке он сохраняется (под капотом), а recycler показывает только не удаленные.
В результате, потом можно восстановить удалённый элемент.

### Expandable
Ячейка, которая может раскрываться

### Loadable
Данные, которые имеют состояния загрузки

### Scrollable
Сохранение позиции скролла в списке.
Пример: зашел на экран, прокрутил - через n минут зашел - восстановил позицию скролла.

### Selectable
Одиночное выделение (аналог radio)

### Visible
Одно и тоже, что и Deletable. Но чтобы не запутаться, используются разные названия.

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:util-ktx:X.X.X"
```