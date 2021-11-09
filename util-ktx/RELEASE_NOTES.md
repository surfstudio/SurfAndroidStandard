# Util-ktx Release Notes

- [0.5.0](#050)
- [0.4.0](#040)
- [0.3.0](#030)
- [0.2.1](#021)

## 0.5.0-alpha
* Update SdkUtils for Android 12
* Update SdkUtils
## 0.5.0
##### Util-ktx
* `DateUtil.parseDate ()` methods can receive `NULL` as input parameter. Will return `NULL` in this case
* Added method `DateUtil.reformatDate ()`, which allows to reformat string representation of the date from one format to another
* ANDDEP-687 Changed "com.google.android.material:material" dependency from "api" to "implementation" type 
* ANDDEP-1065 Add Serializable to `SelectableData`, `CheckableData`, `BlockableData`, `DeletableData`, `ExpandableData`, `LoadableData`, `ScrollableData`, `VisibleData`
* ANDDEP-1148 code updated for target sdk 30
* ANDEEP-1062 added methods to convert sp to px and vice versa
* fix ActivityLifecycleListener
## 0.4.0
##### Util-ktx
* ANDDEP-319 Properties of `isAtLeast ...` of class [`SdkUtils`](lib-util-ktx/src/main/java/ru/surfstudio/android/utilktx/util/SdkUtils.kt)
marked as `@ Deprecated`, instead of them you should use the `isAtLeast ... ()` methods, as well as `runOn ... ()`.
## 0.3.0
##### Util-ktx
* ANDDEP-258 - added toggle methods for main wrappers (`CheckableData`,` SelectableData`)
* Added extensions for working with ClipboardManager:
* `copyTextToClipboard ()` - copies text to the clipboard
* ANDDEP-211 added the ability to adjust the time zone shift and get default for the device
* Added ActivityLifecycleListener for convenient use of Application.ActivityLifecycleCallbacks
## 0.2.1
##### Util-ktx
* ANDDEP-79 Added common classes and data wrapper interfaces: BlockableData, CheckableData, DeletableData, ExpandableData, LoadableData, ScrollableData, SelectableData, VisibleData and extension methods on their collection
* ANDDEP-140 TextViewExtensions added public methods:
  * EditText.selectionToEnd()
  * EditText.allowMatch(predicate)
  * EditText.restrictMatch(predicate)