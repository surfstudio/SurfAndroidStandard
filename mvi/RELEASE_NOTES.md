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
* Add event filtering method to EventTransformerList