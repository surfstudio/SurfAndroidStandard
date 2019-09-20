[Главная страница репозитория](../docs/main.md)

[TOC]

# Core MVI Sample
Является демонстрацией возможностей [core-mvi](../core-mvi/) и содержит некоторые файлы для инициализации работы с фреймворком.

**`Внимание!` Модуль находится в стадии активной разработки!**

**`Внимание!` Ответственность за использование модуля в production полностью ложится на вас.**

## Начало работы
1. Необходимо реализовать базовые сущности, используемые в этом подходе: 

    * Reactor
      
    * EventHub 
      
    * Middleware
    
    * RxBinder
    
    Либо скопировать из пакета /ui/base шаблонные классы и методы-расширения. 
   
1. Для того, чтобы DI корректно работал со всеми классами из шаблона, необходимо скопировать код `ReactScreenModule` в `ScreenModule` вашего проекта, либо дополнительно подключать `ReactScreenModule` к компоненту экрана. 

1. Для удобства работы можно подключить Live Templates: они расположены в папке [templates](/templates). Для подключения, следует скопировать файл `core_mvi.xml` по следующему адресу: 

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