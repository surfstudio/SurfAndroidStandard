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
-dontwarn okio.**
-keep class okio.** { *; }
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

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

-keep class * implements ru.surfstudio.android.network.Transformable
-keep class * implements ru.surfstudio.android.network.response.BaseResponse

#crashlytics
-printmapping mapping.txt

#kotlin-reflect
#https://stackoverflow.com/questions/45871970/kotlin-reflect-proguard-smallsortedmap
#todo убрать, если не нужна котлиновская рефликсия
-dontwarn kotlin.reflect.jvm.internal.**

#todo поставить путь, соответствующий проекту
-keep public class ru.surfstudio.standard.**
-keepclassmembers class ru.surfstudio.standard.** { *;}
-keepnames class ru.surfstudio.standard.ui*