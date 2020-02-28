# Миграция проекта с fabric crashlytics на firebase crashlytics

Делать после [миграции проекта с fabric на firebase app distribution](app_distribution_migration.md).

1. В **app/build.gradle** удалить строки
```
buildscript {
    repositories {
        maven { url 'https://maven.fabric.io/public' }
    }
}
```
Добавить строку `apply plugin: 'com.google.firebase.crashlytics'` после
`apply plugin: 'kotlin-kapt'`
1. В **app/src/main/AndroidManifest.xml** удалить блок
```
<meta-data
        android:name="io.fabric.ApiKey"
        android:value="" /> -->
```
1. В **base/build.gradle** добавить строки
```
api "com.google.firebase:firebase-analytics:$firebaseAnalyticsVersion"
api "com.google.firebase:firebase-messaging:$firebaseMessagingVersion"
```
1. В **base_feature/build.gradle** удалить блок
```
api("com.crashlytics.sdk.android:crashlytics:$crashlyticsVersion@aar") {
    transitive = true
}
```
и в конец блока `dependencies` добавить
```
api("com.google.firebase:firebase-crashlytics:$firebaseCrashlyticsVersion") {
    transitive = true
}
```
1. В **App.kt** вместо кода
```
private fun initFabric() {
    Fabric.with(this, *getFabricKits())
}

private fun getFabricKits() = arrayOf(
        Crashlytics.Builder()
                .core(CrashlyticsCore.Builder()
                        .disabled(BuildConfig.DEBUG)
                        .build())
                .build()
)
```
добавить код
```
private fun initFirebaseCrashlytics() {
    FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(isNotDebug())
}

private fun isNotDebug() = !BuildConfig.BUILD_TYPE.contains("debug")
```
Заменить строку `RemoteLogger.addRemoteLoggingStrategy(CrashlyticsRemoteLoggingStrategy())`
на `RemoteLogger.addRemoteLoggingStrategy(FirebaseCrashlyticsRemoteLoggingStrategy())`

[FirebaseCrashlyticsRemoteLoggingStrategy](../../../template/base_feature/src/main/java/ru/surfstudio/standard/application/logger/FirebaseCrashlyticsRemoteLoggingStrategy.kt)  
находится в шаблоне проекта и добавляется локально.
1. В **build.gradle** уровня проекта удалить строку `maven { url 'https://maven.fabric.io/public' }`
и вместо ` classpath "io.fabric.tools:gradle:$fabricVersion"` добавить
`classpath "com.google.firebase:firebase-crashlytics-gradle:$firebaseCrashlyticsGradleVersion"`
1. Обновить **config.gradle**
```
gradlePluginVersion = '3.5.2'               //https://bit.ly/2NXD4Pe
buildToolsVersion = "29.0.0"                //https://bit.ly/2DNmq3Y
googleServicesVersion = '4.3.3'             //https://bit.ly/2Q5FCge

firebaseMessagingVersion = '20.1.0'         //http://bit.ly/2Q81pGJ
playServicesLocationVersion = '17.0.0'      //http://bit.ly/2CulYrn
firebaseAnalyticsVersion = '17.2.2'         //http://bit.ly/2Q81pGJ
 
firebaseCrashlyticsVersion = '17.0.0-beta01'     //http://bit.ly/2RM7ec2
firebaseCrashlyticsGradleVersion = '2.0.0-beta01'//http://bit.ly/2RM7ec2
```
и удалить
```
fabricVersion = "1.26.1"                    //https://bit.ly/2OOly00
crashlyticsVersion = '2.9.6'                //http://bit.ly/2v5mXbp
```
1. Обновить **proguard-rules.pro**
```
-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.**
-dontwarn com.google.firebase.messaging.**
```
и удалить
```
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
```

[Документация](https://firebase.google.com/docs/crashlytics/upgrade-sdk?authuser=0&platform=android)