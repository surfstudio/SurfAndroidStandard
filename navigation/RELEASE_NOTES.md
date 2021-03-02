# Navigation Release Notes

## 0.5.0-alpha
##### Navigation
* ANDDEP-312 Added navigation module
* ANDDEP-1113 Fixed fragmentManager crash, on adding fragment when app is in background.
* Fragment manager crash fix updated for Xiaomi on Android 9
* ANDDEP-1110 Add removeAll and removeUntil fragment animations
* Fix RemoveUntil command execution for single removal
* Add methods to listen for changes of tab head in TabFragmentNavigator
* Update removeUntil and removeAll animations: now they inherit animations from the first fragment of the removing bunch.
* ANDDEP-1100 Added command StartForResult to open system activities for getting result
* ANDDEP-1103 Added sequential async and sync command execution