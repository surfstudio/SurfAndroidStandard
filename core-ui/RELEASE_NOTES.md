# Core-ui Release Notes

- [0.5.0-alpha](#050-alpha)
- [0.4.0](#040)
- [0.3.0](#030)
- [0.2.1](#021)
- [0.2.0](#020)

## 0.5.0-alpha
##### Core-ui
* ANDDEP-466 Fixed crash when calling `FragmentNavigaotr.popStack` with `popDepth` < 0
* Fix logging screen names
* Fix extra name for settingsNegativeButtonStr in DefaultSettingsRationalActivity
* Add additional Route Extra parameters
* Added android 11 support for PermissionManager (one-time permission handling)
* ANDDEP-985 Fix screen names logging
* ANDDEP-1049
    * **NO BACKWARD COMPATIBILITY** All classes responsible for navigation migrated from `core-ui` to `core-navigation`.
    If your app depends on navigation mechanisms from `core-ui`, add `core-navigation` module in its gradle dependencies as well.
    * **NO BACKWARD COMPATIBILITY** All classes responsible for permissions migrated from `core-ui` to `permission`
    If your app depends on permission handling mechanisms from `core-ui`, add `permission` module in its gradle dependencies as well.
* ANDDEP-1048 Fixing wrong docs links and docs structure
* Added method to 'FragmentNavigator' for checking that fragment already exists in back stack
* Added method to 'FragmentNavigator' for creating new fragment or get fragment from back stack and show him
* Fix TabFragmentNavigator.kt, a transaction for replace must be executed at the end of the main thread queue
* Fix TabFragmentNavigator.kt, a transaction for pop stack must be executed at the end of the main thread queue
## 0.4.0
##### Core-ui
* Added the ability to set text for the buttons of the standard dialog for switching to settings.
## 0.3.0
##### Core-ui
* ANDDEP-220 - fixed the `TabFragmentNavigator` bug with adding to the backend
* [`PermissionManager`](../permission/lib-permission/src/main/java/ru/surfstudio/android/core/ui/permission/PermissionManager.kt):
* rewritten in Kotlin
* `PermissionManagerFor ...` the constructors have changed - now they accept in addition: SharedPrefs and Navigator
* Added class [`PermissionStatus`](../permission/lib-permission/src/main/java/ru/surfstudio/android/core/ui/permission/PermissionStatus.kt) - wraps the permission check response. Now `check (permissionRequest: PermissionRequest): PermissionStatus`.
* Extended functionality: new fields in [`PermissionRequest`](../permission/lib-permission/src/main/java/ru/surfstudio/android/core/ui/permission/PermissionRequest.kt),
the ability to set up a dialogue explaining the reasons for requesting permission,
the ability to show the need to go to the phone settings;
## 0.2.1
##### Core-ui
* Fixed crash of IllegalStateException "Can't find Persistent scope ..." when switching from a restored screen containing fragments in detached state
* ANDDEP-198 The shouldShowRequestPermissionRationale method has been added to the PermissionManager, which allows to understand whether the permissions request dialog is still displayed or is it already prohibited
## 0.2.0
##### Core-ui
* ANDDEP-108  Realize TabFragmentNavigator by detach/attach