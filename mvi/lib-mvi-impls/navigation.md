[Главная страница репозитория](../docs/main.md)

[TOC]

# Навигация
За всю навигацию в mvi отвечает `NavigationMiddleware` (`NavigationMiddlewareInterface`).

Механзим работы этого middleware следующий: 
Он принимает события, унаследованные от OpenScreenEvent / CloseScreenEvent, извлекает Route из этих событий, и направляет их в 
`ScreenNavigator`. 

`ScreenNavigator`, содержит все стандартные навигаторы (activityNavigator, fragmentNavigator, dialogNavigator), в зависимости от того, какой Route ему передан (Activity, Fragment, Dialog, итд) решает, какой навигатор использовать, и какой экран тот откроет. 

TODO подробнее описать `listenForResult`.
