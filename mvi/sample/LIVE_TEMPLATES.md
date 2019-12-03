 Для удобства работы можно подключить Live Templates: они расположены в папке [templates][templates_dir]. Для подключения, следует скопировать файл `core_mvi.xml` по следующему адресу: 

    Windows: `<your_user_home_directory>\.AndroidStudio<version_number>\config\templates`

    Linux: `~/.AndroidStudio<version>/config/templates`

    macOS: `~/Library/Preferences/AndroidStudio<version>/templates`

И исправить импорты базовых классов на те, что используются в проекте.

Список доступных шаблонов: 
    
* `roe` - reactOnEvent. Служит для быстрого написания реакции кода в when-выражении у reactor:

        is $name$ -> on$name$(holder, event)
        is FilterNumbers -> onFilterNumbers(holder, event)
    
    Для удобства так же можно сразу же после автодополнения с помощью сочетания клавиш alt+enter создать метод, в котором будет производится обработка события.

* `tre` - transformEvent. Трансформация события в BaseMapMiddleware

* `genmapmw` - Generate Map Middleware. Генерация BaseMapMiddleware.

* `genev` - Generate Event. Генерация базового класса события с ЖЦ.

* `genbasemw` - Generate Base Middleware.Генерация BaseMiddleware

* `gennavmw` - Generate Navigation Middleware.Генерация BaseNavMiddleware

* `genreactor` - Generate reactor and holder. Генерация базовых классов Reactor и StateHolder

* `providedep` - Provide Dependencies. Генерация кода для модуля в конфигураторе: provide-метод для ScreenEventHub и provide-метод для ScreenBinder.

[templates_dir]: /templates