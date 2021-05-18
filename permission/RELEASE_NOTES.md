# Permission Release Notes

- [0.5.0](#050)

## 0.5.0
##### Permission
* ANDDEP-1049 All classes responsible for permissions migrated from `core-ui` to `permission`
* **NO BACKWARD COMPATIBILITY** ANDDEP-1049 Package for `BaseActivityResultDelegate`,`SupportOnActivityResultRoute` and `CrossFeatureSupportOnActivityResultRoute` is changed 
from `ru.surfstudio.android.core.ui.event.result` to:  `ru.surfstudio.android.core.ui.navigation.event.result`
* ANDDEP-1048 Fixing wrong docs links and docs structure
* ANDDEP-1148 code updated for target sdk 30
* **NO BACKWARD COMPATIBILITY** ANDDEP-1201 removed core-navigation from dependencies, instead use
  new navigation module. Now there is only one PermissionManager which should be used as singleton.
  Added check for grant once.