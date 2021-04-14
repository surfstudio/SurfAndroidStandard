# Core-mvp-binding Release Notes

- [0.5.0-alpha](#050-alpha)
- [0.3.0](#030)

## 0.5.0-alpha
##### Core-mvp-binding
* ANDDEP-1207 extracted relation's source/target interfaces
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
* ANDDEP-968 `Loading.kt`: added class `SimpleLoading`;
* ANDDEP-968 `RequestUi.kt`: added fields `isLoading`, `hasData`, `hasError`;
* ANDDEP-1048 Fixing wrong docs links and docs structure

##### Core-mvp-binding-tests
* TODO
## 0.3.0
##### Core-mvp-binding
* Renamed `onViewDetached ()` -> `onViewDetach ()`