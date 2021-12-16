# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#rx
-dontwarn rx.**
-keep class rx.** { *; }

#retrofit / okhttp
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keep class okio.** { *; }
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.internal.platform.**
-dontwarn okio.**
-dontwarn org.conscrypt.**

#gson
-keepattributes SerializedName
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers enum * { *; }

-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-dontwarn javax.annotation.concurrent.GuardedBy
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

#guava
-dontwarn afu.org.checkerframework.checker.formatter.**
-dontwarn afu.org.checkerframework.checker.nullness.**
-dontwarn afu.org.checkerframework.checker.regex.**
-dontwarn afu.org.checkerframework.checker.units.**

#TODO Поменять пути в соответствии с реальным расположением файлов.
#network
-keep class * implements ru.surfstudio.standard.i_network.network.Transformable
-keep class * implements ru.surfstudio.standard.i_network.network.response.BaseResponse
-keepclassmembers,allowobfuscation class * { @com.google.gson.annotations.SerializedName <fields>; }

#glide
-dontwarn com.bumptech.glide.**

#firebase crashlytics
-printmapping mapping.txt
-keepattributes *Annotation*,SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.**
-dontwarn com.google.firebase.messaging.**

#kotlin-reflect
#https://stackoverflow.com/questions/45871970/kotlin-reflect-proguard-smallsortedmap
-dontwarn kotlin.reflect.jvm.internal.**

#android standard
-keep class ru.surfstudio.android.rx.extension.ConsumerSafe { *; }
-keep class ru.surfstudio.android.rx.extension.ActionSafe { *; }

#cross feature fragments
-keep interface ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment {*;}
-keep class * implements ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment