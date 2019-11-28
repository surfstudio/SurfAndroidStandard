[TOC]
# Core-mvp-binding Release Notes
## 0.5.0-alpha
##### Core-mvp-binding
* ANDDEP-687 Added "androidx.constraintlayout:constraintlayout" dependency with "implementation" type
* Clearing `BaseRxFragmentView.viewDisposable` move from onDestroy to onDestroyView
* ANDDEP-671 Renamed loadable to response
## 0.3.0
##### Core-mvp-binding
* Renamed `onViewDetached ()` -> `onViewDetach ()`
* AndroidSchedulers.mainThread() replaced with ImmediateScheduler 
 in subscription methods of BaseRxViews.