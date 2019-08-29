[TOC]
# Android-standard-template Release Notes
## 0.4.0
##### Android-standard-template
* Updated navigation mechanisms in template
* ANDDEP-323 Added by Chuck
    * Chuck integration in template
    * Added the ability to turn it on / off on DebugScreen
* ANDDEP-336 Add TinyDancer to debug screen
    * Added TinyDancer library to display FPS
* ANDDEP-335 Added by Stetho on DebugScreen
    * Added the Stetho library which in conjunction with Google Chrome can be used for debugging.
* Added version labels on application icons in the launcher
* Added switch between the main and test server
* Added the ability to open Developer Tools via DebugScreen
* Added by LeakCanary
* Added application file viewer viewer
* Added Build scans plugin https://guides.gradle.org/creating-build-scans/
* Added ability to add delay to query execution
    * On the DebugScreen screen in the server settings, you can add a request delay of 0c 0.5c 1c 2c 4c 8c
* ANDDEP-444 Dagger dependencies removed from [`AppComponent`] (template / base_feature / src / main / java / ru / surfstudio / standard / application / app / di / AppComponent.kt)
and [`ActivityComponent`] (template / base_feature / src / main / java / ru / surfstudio / standard / ui / activity / di / ActivityComponent.kt)
into separate classes: [`AppProxyDependencies`] (template / base_feature / src / main / java / ru / surfstudio / standard / application / app / di / AppProxyDependencies.kt)
and [`ActivityProxyDependencies`] (template / base_feature / src / main / java / ru / surfstudio / standard / ui / activity / di / ActivityProxyDependencies.kt),
who are now responsible for distributing dependencies between components.
## 0.3.0
##### Android-standard-template
* ANDDEP-272 - DebugScreen
* ANDDEP-250 - assembly types are moved to a separate gradle file
* ANDDEP-254 - added application signature mechanism - keystore directory.
* ANDDEP-255 - created a minimal test environment for testing without an emulator (Robolectric)
* Added the ability to connect modules locally. Description [here] (template / android-standard / README.md)