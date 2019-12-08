[Главная страница репозитория](../docs/main.md)

[TOC]

# Core MVI Implementations
Этот модуль содержит самые базовые реализации mvi-сущностей, которые могут 
либо сразу использоваться, либо расширяться на проектах. 

## Базовые реализации

### ScreenEventHub 
Реализация EventHub, поддерживающая автоматическое подхватывание событий жизненного цикла View, OnNewIntent и OnBackPressed.

### BaseMiddleware 
Базовый Middleware для работы с экраном. Содержит в себе логику добавления к Observable модификаторов с обработкой ошибки через ErrorHandler и переводом в io-поток,  
а так же механизм DSL и реакцию на события жизненного цикла View.

### BaseMapMiddleware
Базовый Middleware с простейшим механизмом трансформаций как flatMap. 

### NavigationMiddleware
Middleware, инкапсулирющий в себе логику трансформации событий навигации. Более подробно можно почитать в [Readme по навигации][navreadme].

### EventTransformerList 
Список, в котором содержатся все базовые трансформации потока событий. 
Используется для создания DSL-синтаксиса. 
Если необходимо добавить дополнительные трансформации в список, нужно унаследоваться от этого класса, и явно указать тип нового списка в вашем базовом Middleware.

### Screen Binder
Реализация RxBinder, которая связывает все переменные на экране, и поддерживает заморозку/разморозку событий с помощью SubscriptionFreezer. 

# Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru)
необходимо, чтобы корневой `build.gradle` файл проекта был сконфигурирован так,
как описано [здесь](https://bitbucket.org/surfstudio/android-standard/overview).

Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.android:mvi-impls:X.X.X"
```

[navreadme]: navigation.md