# File storage
Простое обьектное хранилище на основе файлов. Хранение данных происходит в формате key(String) - value(Object), где key используется как имя файла, а в качестве содержимого выступает value, сериализованный в массив байт.

Может использоваться для реализации кэша.

#### Использование
Наследованием от [`BaseFileStorage`][bfs] или более специфичных его реализаций:

* [`BaseTextLocalCache`][btfs]
* [`BaseJsonFileStorage`][bjfs]
* [`BaseSerializableFileStorage`][bsfs]

Наследник определяет папку, в которой будут храниться файлы. 

**Важно**: для имени папки не следует использовать имя класса из-за обфускации.

Внутренние сущности:

* ObjectConverter - сериализатор/десериализатор, есть готовые реализации для Serializable, Json
* NamingProcessor - преобразует ключ в имя файла, необходим из-за того, что ключ может быть любого размера а размер имени файла ограничен системой
* FileProcessor - инкапслулирует действия с файлами внутри некоторой директории
* Encryptor - позволяет шифровать сохраняемые данные 

[Пример использования](../filestorage-sample)

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:filestorage:X.X.X"
```

[bfs]:  src/main/java/ru/surfstudio/android/filestorage/storage/BaseFileStorage.java
[btfs]:  src/main/java/ru/surfstudio/android/filestorage/storage/BaseTextFileStorage.kt
[bjfs]:  src/main/java/ru/surfstudio/android/filestorage/storage/BaseJsonFileStorage.kt
[bsfs]:  src/main/java/ru/surfstudio/android/filestorage/storage/BaseSerializableFileStorage.kt