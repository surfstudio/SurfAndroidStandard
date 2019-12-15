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
 * Add BufferedCommand to deliver commands even when view is destroyed
## 0.3.0
##### Core-mvp-binding
* Renamed `onViewDetached ()` -> `onViewDetach ()`