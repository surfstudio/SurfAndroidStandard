# TODO Добавить ссылки на классы и примеры, добавить описания к пунктам по возможности.
# Security
Используется для обеспечения безопасности приложения.

# Использование
#### Основные классы:

1. [AppDebuggableChecker](../security/src/main/java/ru/surfstudio/android/security/app/AppDebuggableChecker.kt)- класс, проверяющий debuggable-флаги приложения при его запуске.
2. [RootChecker](../security/src/main/java/ru/surfstudio/android/security/root/RootChecker.kt) - проверяет наличие рут-прав на устройстве.
3. [KeyEncryptor](../security/src/main/java/ru/surfstudio/android/security/crypto/KeyEncryptor.kt) - абстрактный класс для реализации безопасного [Encryptor'a](../filestorage/src/main/java/ru/surfstudio/android/filestorage/encryptor/Encryptor.kt).
4. [CertificatePinnerCreator](../security/src/main/java/ru/surfstudio/android/security/ssl/CertificatePinnerCreator.kt) - класс, создающий CertificatePinner для OkHttpClient для реализации ssl-pinning.

[Пример использования модуля](../security-sample)

#### Подключение:
###### Gradle:
```
    implementation "ru.surfstudio.android:security:X.X.X"
```

# Security tips
1. В приложении следует использовать только Explicit [Intent](https://developer.android.com/reference/android/content/Intent). Для открытия [Activity](https://developer.android.com/guide/components/activities/intro-activities), работы с [Service](https://developer.android.com/reference/android/app/Service), [BroadcastReceiver](https://developer.android.com/guide/components/broadcasts).
Implicit Intent следует использовать, когда необходимо запустить компонент не являющийся частью вашего приложения: Email клиент, Карты, Sharing данных.
2. BroadcastReceiver, Service, ContentProvider - должны иметь флаг exported = false, дабы предотвраить утечку данных за пределы вашего приложения.
3. Использовать BroadcastReceiver для рассылки Intent, которые не должны выйти за пределы приложения строго не рекомендуется, для этого есть [LocalBroadcastManager](https://developer.android.com/reference/android/support/v4/content/LocalBroadcastManager).
4. Логгирование должно работать **только на debuggable** сборках.
5. Для релизных версий должен быть подключен [ProGuard](https://jebware.com/blog/?cat=16)
6. Любая [Sensitive data](https://en.wikipedia.org/wiki/Information_sensitivity) должна храниться в защищенном [internal storage](https://developer.android.com/training/data-storage/files).
7. Пароли, пинкоды и.т.п строго не рекомендуется хранить на устройстве. Если всё же это необходимо - такие данные должны быть зашифрованы. См. п. 6.
8. Activity с важной информацией должны быть защищены с помощью флага [FLAG_SECURE](https://developer.android.com/reference/android/view/WindowManager.LayoutParams).
Флаг запрещает делать скриншоты с этого Activity и в свернутом виде контент окна тоже не будет отображаться.
9. Финансовые приложения, приложения имеющие дело с важными персональными данными пользователя должны иметь проверку на [ROOT](https://ru.wikipedia.org/wiki/Root).
Root права дают возможность залезть в закрытую область памяти, доступную только приложению, где хранится кеш приложения, базы данных, SharedPreferences итп. 
В случае обнаружения root прав на устройстве, приложение должно предупредить пользователя о рисках, связанных с этим.
<br>**Рекомендации при обнаружении рут прав:**
<br> 1. Прекратить хранение важных данных на устройстве.
<br> 2. Отчистить существующие хранилища.
<br> 3. Функционал приложения связанный с финансовыми операциями, работой с персональными и секретными данными должен быть ограничен/отключен.
<br> 4. Полностью прекратить доступ пользователя в приложение.
<br> Проверку root прав следует делать при помощи NDK, т.к проверка будет проходить на более низком уровне.
Использование проверки при помощи Java кода, не эффективна, т.к существуют способы ["спрятать"](https://github.com/devadvance/rootcloak) root права от проверяющего кода на Java.
10. Приложение должно быть защищено от debug'a. [Пример использования AppDebuggableChecker](../security-sample/src/main/java/ru/surfstudio/android/security/sample/app/CustomApp.kt)
11. Поля ввода секретных данных, должны поддерживать валидацию введеных данных с помощью Regex, InputFilter, ограничения ввода спецсимволов и.т.п.
<br>Данная мера поможет минимизировать риск [SQL injection](https://ru.wikipedia.org/wiki/Внедрение_SQL-кода)
Также у полей должны отсутствовать пункты "копировать", "вырезать" в контекстом меню, остальные пункты по требованиям к приложению. См. [Пример использования](../security-sample)
13. Захардкоженные ключи рекомендуется хранить в нативном коде при помощи NDK.
13. Для ввода пинкода рекомендуется использовать кастомную клавиатуру.
14. Для дополнительной безопасности сетевого соединения используется SSL pining (Certificate pinning).
Certificate pinning – это внедрение SSL сертификата, который используется на сервере, в код мобильного приложения.
В этом случае приложение будет игнорировать хранилище сертификатов устройства, 
полагаясь только на свое хранилище и позволяя создать защищенное SSL соединение с хостом, подписанным только сертификатом, что хранится в самом приложении.
<br>

