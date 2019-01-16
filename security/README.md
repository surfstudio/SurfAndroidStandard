# Security
Используется для обеспечения безопасности приложения.

# Использование
#### Основные классы:

1. AppDebuggableChecker- класс, проверяющий debuggable-флаги приложения при его запуске.
2. RootChecker - проверяет наличие рут-прав на устройстве.
3. SecureStorage - защищенное хранилище с AES-шифрованием.
4. CertificatePinnerCreator - класс, создающий CertificatePinner для OkHttpClient для реализации ssl-pinning.

[Пример использования](../security-sample)

#### Подключение:
###### Gradle:
```
    implementation "ru.surfstudio.android:security:X.X.X"
```

# Security tips
1. В приложении следует использовать только Explicit [Intent](https://developer.android.com/reference/android/content/Intent). Для открытия [Activity](https://developer.android.com/guide/components/activities/intro-activities), работы с [Service](https://developer.android.com/reference/android/app/Service), [BroadCastReceiver](https://developer.android.com/guide/components/broadcasts).
Implicit Intent следует использовать, когда необходимо запустить компонент не являющийся частью вашего приложения: Email клиент, Карты, Sharing данных.
2. BroadCastReceiver, Service, ContentProvider - должны иметь флаг exported = false, дабы предотвраить утечку данных за пределы вашего приложения.
3. Использовать BroadCastReceiver для рассылки Intent, которые не должны выйти за пределы приложения строго не рекомендуется, для этого есть [LocalBroadcastManager](https://developer.android.com/reference/android/support/v4/content/LocalBroadcastManager).
4. Логгирование должно работать **только на debuggable** сборках.
5. Для релизных версий должен быть подключен [ProGuard](https://jebware.com/blog/?cat=16)
6. Любая [Sensitive data](https://en.wikipedia.org/wiki/Information_sensitivity) должна храниться в защищенном [internal storage](https://developer.android.com/training/data-storage/files).
7. Пароли, пинкоды и.т.п строго не рекомендуется хранить на устройстве. Если всё же это необходимо - такие данные должны быть зашифрованы. См. п. 6.
8. Activity с важной информацией должны быть защищены с помощью флага [FLAG_SECURE](https://developer.android.com/reference/android/view/WindowManager.LayoutParams).
Флаг запрещает делать скриншоты с этого экрана и в свернутом виде контент окна тоже не будет отображаться.
9. Финансовые приложения, приложения имеющие дело с важными персональными данными пользователя должны иметь проверку на [ROOT](https://ru.wikipedia.org/wiki/Root).
В случае обнаружения root'a на устройстве, приложение должно предупредить пользователя о рисках, связанных с этим.
<br>**Рекомендации при обнаружении рут прав:**
<br> 1. Прекратить хранение важных данных на устройстве.
<br> 2. Отчистить существующие хранилища.
<br> 3. Функционал приложения связанный с финансовыми операциями, работой с персональными и секретными данными должен быть ограничен/отключен.
10. Поля ввода секретных данных, должны проходить валидацию при вводе. Regex, InputFilter и.т.п.
Также у полей должны отсутствовать пункты "копировать", "вырезать" в контекстом меню, остальные пункты по требованиям к приложению. См. [Пример использования](../security-sample)
11. Захардкоженные ключи рекомендуется хранить в нативном коде при помощи NDK.
12. Для ввода пинкода рекомендуется использовать кастомную клавиатуру.


