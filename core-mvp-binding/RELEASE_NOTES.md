[TOC]
# Core-mvp-binding Release Notes
## 0.5.0-alpha
##### Core-mvp-binding
* ANDDEP-687 Added "androidx.constraintlayout:constraintlayout" dependency with "implementation" type
* Clearing `BaseRxFragmentView.viewDisposable` move from onDestroy to onDestroyView
* ANDDEP-671 Renamed loadable to response
* Fixed RequestState's observeHasError method + added observeOptionalData
* AndroidSchedulers.mainThread() replaced with MainThreadImmediateScheduler
 in subscription methods of BaseRxViews.
* Added description of two-way binding problem
* Make State open for inheritance
* **NO BACKWARD COMPATIBILITY** ANDDEP-997 Changed package of `Request.kt`.
Added state checker methods to Request.
`asRequest()` extensions moved to separate file `RequestRxExtension.kt`
## 0.3.0
##### Core-mvp-binding
* Renamed `onViewDetached ()` -> `onViewDetach ()`