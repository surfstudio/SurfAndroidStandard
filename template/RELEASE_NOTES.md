[TOC]
# Template Release Notes
## 0.5.0-alpha
##### Template
* ANDDEP-687 Added "androidx.constraintlayout:constraintlayout" dependency with "implementation" type to f-debug
* ANDDEP-687 Added "com.google.dagger:dagger" dependency with "implementation" type to f-debug
* ANDDEP-687 Added "javax.inject:javax.inject" and "com.annimon:stream" to commonModule.gradle
* ANDDEP-687 Removed "com.annimon:stream" dependency from app-module
* ANDDEP-329 Added RxJava2Debug
* ANDDEP-702 Fixed script for local connection of android standard
* ANDDEP-413 Added transition to WindowVQA on DebugScreen
* ANDDEP-459 Added transition to application settings on DebugScreen
* Fixed debug screen push notification appearing on each App#onCreate invocation
* ANDDEP-770 Fixed textStyle overriding and proguard bugs
* Added `PostfixEditText` to base_feature
* Add color styling to generateDependencyGraph task
* ANDDEP-392 Add firebase app distribution and firebase crashlytics
* ANDDEP-927 Add FirebaseCrashlyticsRemoteLoggingStrategy
* ANDDEP-936 Remove Deprecated annotation from i-network module
* ANDDEP-392 Fix firebase crashlytics initialization
* ANDDEP-938 Add link of instruction of project initialization
* ANDDEP-911 Kotlin code style auto-formatting support
* ANDDEP-957 Fix dependency substitution for debug mode
* ANDDEP-732 Fix version-plugin configuration for debug mode
* ANDDEP-937 Fix setting label to app icon
* ANDDEP-961 Add skipSamplesBuild flag for local android standard connection
* ANDDEP-961 Small fixes

## 0.4.0
##### Template
* Updated navigation mechanisms in template
* ANDDEP-323 Added Chuck
* ANDDEP-336 Added TinyDancer
* ANDDEP-335 Added Stetho
* Added version labels on application icons in the launcher
* Added switcher between main and test servers
* Added switch between main and test server
* Added LeakCanary
* Added viewer of file storage of application
* Added plugin [`Build scans`](https://guides.gradle.org/creating-build-scans/)
* Added the ability to add a request execution delay
  * On the DebugScreen screen in the server settings section, you can add a request delay of 0s, 0.5s, 1s, 2s, 4s, 8s
* ANDDEP-444 Dagger dependencies taken out of [`AppComponent`](template/base_feature/src/main/java/ru/surfstudio/standard/application/app/di/AppComponent.kt)
and [`ActivityComponent`](template/base_feature/src/main/java/ru/surfstudio/standard/ui/activity/di/ActivityComponent.kt)
in separate classes:  [`AppProxyDependencies`](template/base_feature/src/main/java/ru/surfstudio/standard/application/app/di/AppProxyDependencies.kt)
and [`ActivityProxyDependencies`](template/base_feature/src/main/java/ru/surfstudio/standard/ui/activity/di/ActivityProxyDependencies.kt),
which are now responsible for distributing dependencies between components.
* SBB-1862 Added module cf-pagination

## 0.3.0
##### Template
* ANDDEP-272 DebugScreen
* ANDDEP-250 Assembly types are moved to a separate gradle file
* ANDDEP-254 Added application signature mechanism - keystore directory.
* ANDDEP-255 Created a minimal test environment for testing without an emulator (Robolectric)
* Added the ability to connect modules locally. Description is [here](template/android-standard/README.md)