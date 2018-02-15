# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/ironz/Documents/android-sdk-linux/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class httpMessage to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#rx
-dontwarn rx.**
-keep class rx.** { *; }

#lambda
-dontwarn java.lang.invoke.**
-keep class java.lang.invoke.** { *; }


#gson
-keepattributes SerializedName
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }
-keepclassmembers enum * { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keep class ru.surfstudio.android.core.domain.Unit {*;}
-keep class * implements ru.surfstudio.android.network.Transformable
-keep class * implements BaseResponse

#retrofit / okhttp
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-dontwarn okio.**
-keep class okio.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

# crash logs
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# lombok
-dontwarn java.beans.**

#other
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement


-dontnote android.net.http.*
-dontnote org.apache.commons.**
-dontnote org.apache.http.**