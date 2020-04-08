[TOC]
# Core-mvi Release Notes
## 0.5.0-alpha
##### Core-mvi
* ANDDEP-671 Core mvi refactor, add comments
* ANDDEP-671 Add navigation middleware, add dsl
* Divide Mvi Core and Mvi Implementation
* Add base mvi entities to mvi-impls module
* Refactor screen navigation, add NavigationMiddleware
* Add CompositionEvent and event composition principle, navigation composition
* Add empty trigger transformation to BaseMiddleware to fix specific behavior: 
if Middleware doesn't contain any transformations, Reactor.react method won't be triggered, 
even if stream contains events from UI, which should be reacted directly.
* Add examples and additional docs
* Add Reducer StateHolder implementations
* Add customizable EventHub logger 
* Add .gitignore to mvi/lib-mvi-impls
* Add CustomAlertDialog based on Events.
* Move logging to "ScreenEventHub.emit" method.
* Add event filtering method to EventTransformerList
* Add additional schemes to docs
* Remove listenForResult extension for EventTransformerList + listenForResult from NavigationMiddleware
* Fixed StandardReactDialogView negative button nullability
* ANDDEP-928 Remove Timber dependencies
* **NO BACKWARD COMPATIBILITY** ANDDEP-997 Renamed RequestEvent.type to RequestEvent.request.
Replaced RxMiddleware.asRequestEvent `Event` parameter with `EventFactory`.
* ANDDEP-968 `Reducer.kt`: moved to `mvi-reducer` module;
##### Mvi-reducer
* ANDDEP-968 **NEW** `RequestMapper.kt`: class for managing requests;
* ANDDEP-968 **NEW** `RequestMapperLambdas.kt`: file that holds typealiases for `RequestMapper`;
##### Mvi-sample
* ANDDEP-968 New sample screens: `KittiesActivity` and `KittiesAllActivity`;
