[Главная](/docs/main.md)

# Analytics
- [Использование](#использование)
- [Начиная с версии 0.4.0](#начиная-с-версии-040)
- [Подключение](#подключение)

Содержат в себе набор классов для выполнения дейсвтвия в сервисах аналитики (отправка события, вызов метода аналитики и прочее)
Имеет только обобщенные интерфейсы без конкретной реализации. Позволяет работать с любыми конкретынми сервисами 
аналитики в обобщенной манере (Firebase, Flurry, AppMetrica, etc)

# Использование
[Пример использования](../sample)

Основные классы:
* [`AnalyticAction`][aa]
* [`AnalyticActionPerformer`][aap]
* [`HasKey`][hk]
* [`DefaultAnalyticSercvice`][das]

Набор классов не пытается предусмотреть все возможные потребности при реализации конкретного сервиса аналитики. 
Вместо этого используются асбтракции как 'Действие аналитики' ([`AnalyticAction`][aa]) и 'Выполнитель действия аналитики' ([`AnalyticActionPerformer`][aap])
Под действием понимается вызов любого метода сервиса аналитики. Конкретный выполнитель это действие должен преобразовать
данные действия (на сами данные никаких ограничений нет) в формат требуемый конкретным сервисом. Смотрите [Пример использования FirebaseAnalytics](../sample)

# Начиная с версии 0.4.0

Старые события продолжают работать без изменения. Для использования новых возможстей либо перевода старых события на новый набор классов,
смотрите добавление аналитики в проект.

Gradle:

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:analytics:X.X.X"
```

[aa]: src/main/java/ru/surfstudio/android/analyticsv2/core/AnalyticAction.kt
[aap]: src/main/java/ru/surfstudio/android/analyticsv2/core/AnalyticActionPerformer.kt
[hk]: src/main/java/ru/surfstudio/android/analyticsv2/HasKey.kt
[das]: src/main/java/ru/surfstudio/android/analyticsv2/DefaultAnalyticSercvice.kt