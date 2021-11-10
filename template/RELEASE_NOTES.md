# Template Release Notes

- [0.5.0-alpha](#050-alpha)
- [0.4.0](#040)
- [0.3.0](#030)

## 0.5.0-alpha
##### Template
* Remove jcenter()
* Remove MaterialProgressBar
* Template updates:
  * Fix BasePushHandleStrategy (make push notifications visible by default)
  * Remove PostfixEditText (see PrefixPostfixEditText in code templates project)
  * Update service layer for SurfGen usage
  * Message controller dependency is used for debug module only
  * Clean f-debug-no-op dependencies
  * Remove imageloader, use Glide or other library instead
  * Update versions
  * Remove firebase.core dependency
  * Add base_resources module
* ANDDEP-1211 Add onboarding to template
* ANDDEP-1204 Added Danger and Detekt to the template
* ANDDEP-1208 Removing kotlin.androidx.synthetic from template
* ANDDEP-1206 add best practice showing snackbar to template
* ANDDEP-1149 Added RxMiddlewareExtensions to the template
* ANDDEP-1205 added ToolbarConfig in template
* Add maven central URL
* ANDDEP-1203 Fixes after core-ui improvements
* Added method for mocking domain models
* Added unit-tests for SplashMiddleware and MainBarMiddleware
* Added utility classes for unit testing
* `kotlin` version raised: `1.3.71 -> 1.4.31`;
* ANDDEP-1171 ResourceProvider moved to core-ui
* ANDDEP-1040 fixed template build stage
* ANDDEP-46 Added default BottomBarView implementation
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
* ANDDEP-928 Add RemoteLogger
* ANDDEP-980 Fix template build
* ANDDEP-1002 app module dependencies refactoring
* ANDDEP-964 Refactored dependency imports in the project template
* ANDDEP-982 `kotlin targetJvm` version raised: `1.6 -> 1.8`;
* ANDDEP-982 `gradle-wrapper` version raised: `5.4.1 -> 5.6.4`;
* ANDDEP-982 `kotlin` version raised: `1.3.31 -> 1.3.71`;
* ANDDEP-982 `leakCanary` version raised: `1.6.2 -> 2.2`. Migration done;
* ANDDEP-982 `okHttp` version raised: `3.12.0 -> 4.4.1`. Migration done;
* ANDDEP-982 `easyadapter` now depends on `androidx.asynclayoutinflater`;
* ANDDEP-982 other libraries versions changed;
* ANDDEP-915 Added `validateCrossFeatureRoutes.gradle.kts` task. **Note**: supported only `.kt` source files parsing;
* ANDDEP-599 Added `CompletableSafeConverter` to parse not empty Completable-request response;
* ANDDEP-1038 Remove `PaginationableAdapter` and add
  `PaginationFooterItemController` instead for using
  `easyadapter-pagination` module
* ANDDEP-1057 Update androidStandardVersion
* ANDDEP-1039 Removed extra .gitignore and proguard-rules.pro files
* ANDDEP-1049 Added `permission` and `core-navigation` dependencies to template.
* ANDDEP-1048 Fixing wrong docs links and docs structure
* Fixed fast switching states between NONE and other
* ANDDEP-1112 Fixed method name typo
* ANDDEP-1112 Fixed access modifier in `SafeConverterFactory.Function`
* ANDDEP-1112 Fixed generic type in `CompletableSafeConverter`
* ANDDEP-1062 added view KeyboardView
* ANDDEP-1109 Added new obfuscation rule and TODO in [`proguard-rules.pro`](proguard-rules.pro)
* ANDDEP-1111 Added class inheritance: `Transformable` extends `BaseResponse`
* ANDDEP-1099 Added new navigation validation support
* ANDDEP-1136 Architecture changed to MVI
* ANDDEP-1137 There is no support for FreeMarker templates in AndroidStudio 4.1, so file templates were modified in order to use them with Geminio library. Geminio compatible file templates are added to project template.
* ANDDEP-1126 Fixed MigrationModule.kt, get version code from packageInfo.
* ANDDEP-930 Added handling custom notification actions
* ANDDEP-1138 Added supporting View Binding
* ANDDEP-1144 Fix navigation
* ANDDEP-1135 Added [`SimpleDialogView`](base_feature/src/main/java/ru/surfstudio/standard/ui/dialog/simple/SimpleDialogView.kt) & easy sample to it
* ANDDEP-1141 Added Firebase BoM dependency
* Fix event generation
* ANDDEP-1100 Navigation with starting system activities for result
* ANDDEP-1170 Added IntentChecker
* ScreenState added to BaseMiddlwareDependency
* ANDDEP-1066 Migrated to gradle 6
* Added startFoResult extension for events
* Added base class for bottom sheet dialog
* ANDDEP-1201 updated PermissionManager with new Navigation module
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
* ANDDEP-444 Dagger dependencies taken out of
  [`AppComponent`](base_feature/src/main/java/ru/surfstudio/standard/application/app/di/AppComponent.kt)
  and
  [`ActivityComponent`](base_feature/src/main/java/ru/surfstudio/standard/ui/activity/di/ActivityComponent.kt)
  in separate classes:
  [`AppProxyDependencies`](base_feature/src/main/java/ru/surfstudio/standard/application/app/di/AppProxyDependencies.kt)
  and
  [`ActivityProxyDependencies`](base_feature/src/main/java/ru/surfstudio/standard/ui/activity/di/ActivityProxyDependencies.kt),
  which are now responsible for distributing dependencies between
  components.
* SBB-1862 Added module cf-pagination

## 0.3.0
##### Template
* ANDDEP-272 DebugScreen
* ANDDEP-250 Assembly types are moved to a separate gradle file
* ANDDEP-254 Added application signature mechanism - keystore directory.
* ANDDEP-255 Created a minimal test environment for testing without an emulator (Robolectric)
* Added the ability to connect modules locally. Description is
  [here](android-standard/README.md)