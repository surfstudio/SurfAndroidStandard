# Core-mvi Release Notes

- [0.5.0-alpha](#050-alpha)

## 0.5.0-alpha
##### Core-mvi
* ANDDEP-1008 License added
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
* ANDDEP-968 `RequestEvent.kt`: added fields `isLoading`, `hasData`, `hasError`;
* ANDDEP-1046 Fixed output events observing mechanism in CompositionTransformer: 
now event composition mechanism supports middlewares that can only produces output events.
* ANDDEP-1048 Fixing wrong docs links and docs structure
* Add log before EventHubChainException throwing
##### Mvi-mapper
* ANDDEP-968 **NEW** `RequestMapper.kt`: class for managing requests;
* ANDDEP-968 **NEW** `RequestMapperLambdas.kt`: file that holds typealiases for `RequestMapper`;
* ANDDEP-1048 Fixing wrong docs links and docs structure
##### Mvi-impls
* ANDDEP-969 Added base classes: `BaseReactor` and `BaseReducer`;
* **NO BACKWARD COMPATIBILITY** ANDDEP-1049 Package for `BaseActivityResultDelegate`,`SupportOnActivityResultRoute` and `CrossFeatureSupportOnActivityResultRoute` is changed 
from `ru.surfstudio.android.core.ui.event.result` to:  `ru.surfstudio.android.core.ui.navigation.event.result`
* ANDDEP-1048 Fixing wrong docs links and docs structure
* **NO BACKWARD COMPATIBILITY** Removed ioHandleError() methods and RxBuilderHandleError dependency from BaseMiddleware. Use reducer to handleError instead.
* **NO BACKWARD COMPATIBILITY** ScreenState added to BaseMiddlwareDependency. Do not forget to add it to dependencies in your ScreenModule.provideBaseMiddlewareDependency method.
* Added LifecycleFreezeMiddleware: it freezes certain observable until the screen doesn't moved in desired lifecycleStage. This middleware is connected to BaseMiddleware by default.
* Added PersistentCheckLifecycleMiddleware: it uses screenState to determine whether view was recreated, or entire screen was recreated after process death, and modifies Lifecycle functions by adding additional information to it. This middleware is connected to BaseMiddleware by default/