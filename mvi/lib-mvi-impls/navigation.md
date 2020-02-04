[Главная страница репозитория](../docs/main.md)

[TOC]

# Навигация
За всю навигацию в mvi отвечает `NavigationMiddleware` (`NavigationMiddlewareInterface`).

Механзим работы этого middleware следующий: 
Он принимает события, унаследованные от OpenScreenEvent / CloseScreenEvent, извлекает Route из этих событий, и направляет их в 
`ScreenNavigator`. 

`ScreenNavigator` содержит все стандартные навигаторы (ActivityNavigator, FragmentNavigator, DialogNavigator),
и решает в зависимости от переданного Route (ActivityRoute, FragmentRoute, DialogRoute, итд), какой навигатор использовать, и какой экран тот откроет. 

В случае, когда нам нужно подписываться на результат работы экрана, список действий такой: 

1. Route, который мы используем, необходимо унаследовать от `SupportOnActivityResultRoute<T>

1. Необходимо добавить в список трансформаций listenForResult, типизированный по нужному нам Route, **при создании** экрана. 
Это обязательное условие, и нужно для того, чтобы среагировать на подписку результата работы экрана даже при полном пересоздании родительского экрана.

1. В OpenScreenEvent мы передаем нужный нам Route.

1. После этого, передаем в navigationMiddleware это событие.

## Composition 
Для упрощения переиспользования, NavigationMiddleware можно использовать в middleware родительских экранов через композицию событий.
Механизм подробное описан [здесь][compreadme].

## Схема

Схематично все вышеописанное можно отобразить так:

![Navigation flow diagram]( https://i.imgur.com/vfHcolI.jpg )

[compreadme]: composition.md