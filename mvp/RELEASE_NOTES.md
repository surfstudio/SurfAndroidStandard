# Mvp Release Notes

- [0.5.0-alpha](#050-alpha)
- [0.4.0](#040)
- [0.3.0](#030)
- [0.2.1](#021)

## 0.5.0-alpha
* ANDDEP-793 added component readme
##### Core-mvp
* ANDDEP-1048 Fixing wrong docs links and docs structure
##### Mvp-widget
* ANDDEP-687 Changed "javax.inject:javax.inject" dependency from "api" to "implementation" type
* ANDDEP-687 Changed "androidx.constraintlayout:constraintlayout" dependency from "api" to "implementation" type
* ANDDEP-633 Fixed widget manual destroy method
* ANDDEP-1048 Fixing wrong docs links and docs structure
##### Mvp-dialog
* ANDDEP-687 Changed "javax.inject:javax.inject" dependency from "api" to "implementation" type
* Fix logging screen names
* ANDDEP-922 Fix crash on dismiss simple dialog in child fragment
* **NO BACKWARD COMPATIBILITY** ANDDEP-922 Fix inconsistent dialog
  navigator behavior: CoreSimpleDialog now showing with it's route tag.
  Methods `DialogNavigator.showSimpleDialog`,
  `CoreSimpleDialogInterface.show`,
  `CoreSimpleBottomSheetDialogFragment.show`,
  `CoreSimpleDialogFragment.show` and `SimpleDialogDelegate.show` are
  changed.
* ANDDEP-922 Fix inconsistent dialog navigator behavior: CoreSimpleDialog now showing with it's route tag. Methods `DialogNavigator.showSimpleDialog`, `CoreSimpleDialogInterface.show`, `CoreSimpleBottomSheetDialogFragment.show`, `CoreSimpleDialogFragment.show` and `SimpleDialogDelegate.show` are changed **without backward compatibility support**.
* ANDDEP-985 Fix screen names logging
* ANDDEP-928 Fix SimpleDialogDelegate logs
* ANDDEP-1048 Fixing wrong docs links and docs structure
## 0.4.0
##### Core-mvp
* ANDDEP-320 Ability to dynamically set LoadState, a flexible way to display LoadState
* `BasePresenter` - added the ability to get only the last value from` ObservableOperatorFreeze` using the method
`subscribeTakeLastFrozen`.
##### Mvp-widget
* Added the ability to use widgets in dynamic layout. Eliminated the need to call `init ()` to initialize the widget.
* For use in the recycler, you must use the manual initialization mode. For this, the attribute `enableManualEdit` is created. Also in `onBindViewHoler` you need to call` init (scopeId) `, where scopeId should be based on the data that the item displays.
* ANDDEP-380 Update widgets: now for receiving the widget's unique identifier is answered by the `getWidgetId` method. Instead of `init (scopeId)` in `RecyclerView` follows use `lazyInit`, and override` getWidgetId` based on data from `onBindViewHolder`. To arrange widgets in a static layout you must specify a unique `android: id`.
* Added support for binding in widgets
* The problem with getting context for widgets lying inside the container with the theme attribute has been resolved
##### Mvp-dialog
* TODO
## 0.3.0
##### Mvp-dialog
* ANDDEP-243 - Fix SimpleDialogDelegate - fix invalid key
## 0.2.1
##### Mvp-dialog
* ANDDEP-215 Extend of all dialogs on AppCompatDialog
* ANDDEP-137 Add the ability to modify errorHandler in basePresenter