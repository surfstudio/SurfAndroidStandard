# Shared preference
Модуль c утилитами для `SharedPreferences`

# Использование
[Пример использования](sample)

Для того, чтобы работали `NO_BACKUP_SHARED_PREF`, необходимо:
* Создать файл `backup_scheme.xml`, в котором
```
<?xml version="1.0" encoding="utf-8"?>
<full-backup-content>
    <exclude domain="sharedpref" path="NO_BACKUP_SHARED_PREF.xml" />
</full-backup-content>
```
* Добавить в манифест
```
    android:fullBackupContent="@xml/backup_scheme"
```

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:shared-pref:X.X.X"
```
