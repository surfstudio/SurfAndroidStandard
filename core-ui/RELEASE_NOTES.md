[TOC]
# Core-ui Release Notes
## 0.5.0-alpha
##### Core-ui
* ANDDEP-466 Fixed crash when calling `FragmentNavigaotr.popStack` with `popDepth` < 0
* Fix logging screen names
* Fix extra name for settingsNegativeButtonStr in DefaultSettingsRationalActivity
* Add additional Route Extra parameters
* Add method for checking that fragment already exists at back stack
* Add method for creating new fragment or get fragment from back stack and show him
## 0.4.0
##### Core-ui
* Added the ability to set text for the buttons of the standard dialog for switching to settings.
## 0.3.0
##### Core-ui
* ANDDEP-220 - fixed the `TabFragmentNavigator` bug with adding to the backend
* [`PermissionManager`](lib-core-ui/src/main/java/ru/surfstudio/android/core/ui/permission/PermissionManager.kt):
* rewritten in Kotlin
* `PermissionManagerFor ...` the constructors have changed - now they accept in addition: SharedPrefs and Navigator
* Added class [`PermissionStatus`](lib-core-ui/src/main/java/ru/surfstudio/android/core/ui/permission/PermissionStatus.kt) - wraps the permission check response. Now `check (permissionRequest: PermissionRequest): PermissionStatus`.
* Extended functionality: new fields in [`PermissionRequest`](lib-core-ui/src/main/java/ru/surfstudio/android/core/ui/permission/PermissionRequest.kt),
the ability to set up a dialogue explaining the reasons for requesting permission,
the ability to show the need to go to the phone settings;
## 0.2.1
##### Core-ui
* Fixed crash of IllegalStateException "Can't find Persistent scope ..." when switching from a restored screen containing fragments in detached state
* ANDDEP-198 The shouldShowRequestPermissionRationale method has been added to the PermissionManager, which allows to understand whether the permissions request dialog is still displayed or is it already prohibited
## 0.2.0
##### Core-ui
* ANDDEP-108  Realize TabFragmentNavigator by detach/attach