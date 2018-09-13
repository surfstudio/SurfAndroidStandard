# File storage
Позволяет сохранять и удалять объекты в файлах на устройстве на внутренней или внешней памяти.

Может использоваться для реализации кэша.

#### Использование
Наследованием от [`BaseLocalCache`][blc] или [`BaseTextLocalCache`][btlc]

Реализация кэша подразумевает хранение всех данных в сериализованных
структурах в конкретном файле (key - filename / value - serialized data) в
папке, название которой определяется наследником.
**Важно**: для имени папки не используется имя класса из-за обфускации.

`BaseLocalCache` - базовый класс, которых имплементирует контракт LocalCache работы
с кэшем. Требуется реализовать наследование от данного класса,
которое впоследствии определит требования контракта.

[Пример использования](../filestorage-sample)

todo - шифрование

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:filestorage:X.X.X"
```

[blc]:  src/main/java/ru/surfstudio/android/filestorage/BaseLocalCache.java
[btlc]:  src/main/java/ru/surfstudio/android/filestorage/BaseTextLocalCache.java