[TOC]
# Rx-extension Release Notes
## 0.5.0-alpha
##### Rx-extension
* SBB-2618 Added overloaded methods `ObservableUtil.mergeDelayError()`. Functionally, the methods are similar to `Observable.mergeDelayError()`, with the difference that the subscription is done on a separate `Scheduler`
* ANDDEP-671 Add Any.toObservable() extension method