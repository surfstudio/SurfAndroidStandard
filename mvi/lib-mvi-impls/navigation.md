[Главная страница репозитория](../docs/main.md)

[TOC]

# Навигация
За всю навигацию в mvi отвечает `NavigationMiddleware` (`NavigationMiddlewareInterface`).

Механзим работы этого middleware следующий: 
Он принимает события, унаследованные от OpenScreenEvent / CloseScreenEvent, извлекает Route из этих событий, и направляет их в 
`ScreenNavigator`. 

`ScreenNavigator`, содержит все стандартные навигаторы (ActivityNavigator, FragmentNavigator, DialogNavigator),
и решает в зависимости от переданного Route (ActivityRoute, FragmentRoute, DialogRoute, итд), какой навигатор использовать, и какой экран тот откроет. 

Логика listenForResult выглядит следующим образом: в событие OpenScreenEvent, 
передаваемое в navigationMiddleware,  необходимо добавить route, 
наследуемый от `SupportOnActivityResultRoute<T>`.
При этом, необходимо добавить вызов метода listenForResult, типизированный по этому руту, 
в список трансформаций Middleware **при создании** этого Middleware.

Это нужно для того, чтобы среагировать на подписку результата работы экрана даже при полном пересоздании родительского экрана.

## Composition 
Для упрощения переиспользования, NavigationMiddleware можно использовать в middleware родительских экранов через композицию событий.
Механизм подробное описан [здесь][compreadme].

[compreadme]: composition.md