[TOC]
# Util-ktx Release Notes
## 0.5.0-alpha.1
##### Util-ktx
* Методы `DateUtil.parseDate()` могут принимать `NULL` на вход и возвращают `NULL` в таком случае
* Добавлен метод `DateUtil.reformatDate()`, позволяющий переворматировать строковое представление даты из одного формата в другой 
## 0.5.0-alpha.0
##### Util-ktx
* TODO
## 0.4.0
##### Util-ktx
* ANDDEP-319 Properties of `isAtLeast ...` of class [`SdkUtils`] (util-ktx / src / main / java / ru / surfstudio / android / utilktx / util / SdkUtils.kt)
marked as `@ Deprecated`, instead of them you should use the` isAtLeast ... () `methods, as well as` runOn ... () `.
## 0.3.0
##### Util-ktx
* ANDDEP-258 - added toggle methods for main wrappers (`CheckableData`,` SelectableData`)
* Added extensions for working with ClipboardManager:
     * `copyTextToClipboard ()` - copies text to the clipboard
* ANDDEP-211 added the ability to adjust the time zone shift
     and get default for the device
* Added ActivityLifecycleListener for convenient use of Application.ActivityLifecycleCallbacks
## 0.2.1
##### Util-ktx
* ANDDEP-79 Added common classes and data wrapper interfaces: BlockableData, CheckableData, DeletableData, ExpandableData, LoadableData, ScrollableData, SelectableData, VisibleData and extension methods on their collection
* ANDDEP-140 TextViewExtensions добавлены публичные методы:
    * EditText.selectionToEnd() - перевод каретки в конец
    * EditText.allowMatch(predicate) - фильтрация символов
    * EditText.restrictMatch(predicate) - // -