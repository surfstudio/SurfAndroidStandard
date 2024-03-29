# Navigation Release Notes

- [0.6.0-alpha](#060-alpha)
- [0.5.1](#051)

## 0.6.0-alpha
##### Navigation
* Update ActivityNavigatorWithResultImpl for Android 12 location permission
* ANDDEP-1226 Added `navigation-observer-deprecated` and `navigation-rx-deprecated` modules that grants backward compatibility if enabled along with other `-deprecated` modules in project.

## 0.5.1
##### Navigation
* ANDDEP-1203 Fixes after core-ui improvements
* ANDDEP-312 Added navigation module
* ANDDEP-1113 Fixed fragmentManager crash, on adding fragment when app is in background.
* Fragment manager crash fix updated for Xiaomi on Android 9
* ANDDEP-1110 Add removeAll and removeUntil fragment animations
* Fix RemoveUntil command execution for single removal
* Add methods to listen for changes of tab head in TabFragmentNavigator
* Update removeUntil and removeAll animations: now they inherit animations from the first fragment of the removing bunch.
* ANDDEP-1154 Fixed crashes "You must specify unique tag"
* ANDDEP-1153 Fixed wrong behavior RemoveLast
* ANDDEP-1100 Added command StartForResult to open system activities for getting result
* ANDDEP-1148 code updated for target sdk 30
* ANDDEP-1201 Added command RequestPermission, added getId for ResultRoute