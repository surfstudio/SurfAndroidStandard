# Модули

Репозиторий содержит большое готовых модулей.
Полное описание модулей [здесь](https://bitbucket.org/surfstudio/android-standard/wiki/Modules).

### Модули рекомендуемые для подключения

Рекомендуемый минимальный набор модулей для создания приложения:

- [core-ui](https://bitbucket.org/surfstudio/android-standard/src/master/core-ui) - модуль расширяющий возможности ui части Android Framework. Может быть использован для создания базовых классов, необходимых для реализации паттернов MVP, MVVM
- [core-mvp](https://bitbucket.org/surfstudio/android-standard/src/master/core-mvp) - расширение core-ui для гибридной архитектуры MVP + Presentation Model.
- [core-app](https://bitbucket.org/surfstudio/android-standard/src/master/core-app) - конфигурирует контекст приложения.
- [mvp-dialog](https://bitbucket.org/surfstudio/android-standard/src/master/mvp-dialog) - базовые классы для диалогов использующихся в студии + навигация
- [mvp-widget](https://bitbucket.org/surfstudio/android-standard/src/master/mvp-widget) - кастомные виджеты с поддержкой презенторов
- [easyadapter](https://bitbucket.org/surfstudio/android-standard/src/master/easyadapter) - обертка над RecyclerView.Adapter - стандарт студии
- [dagger-scope](https://bitbucket.org/surfstudio/android-standard/src/master/dagger-scope) - повсеместно использующиеся даггер-скоупы
- [message-controller](https://bitbucket.org/surfstudio/android-standard/src/master/message-controller) - обертка для управления Snackbar
- [converter-gson](https://bitbucket.org/surfstudio/android-standard/src/master/converter-gson) - Gson-конвертер
- [logger](https://bitbucket.org/surfstudio/android-standard/src/master/logger) - утилиты для логгирования
- [network](https://bitbucket.org/surfstudio/android-standard/src/master/network) - базовые классы для работы с сетью

Остальные модули подключаются по мере необходимости.
todo
