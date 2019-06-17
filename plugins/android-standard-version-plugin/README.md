# Android-Standard configuration plugin

Plugin adds new configuration.
It provides ability to connect local android standard in project. 

### Usage

build.gradle(root)
```groovy
buildscript {
    repositories {
        maven { url 'https://artifactory.surfstudio.ru/artifactory/libs-release-local' }
    }

    dependencies {
        classpath 'ru.surfstudio.android:configuration-plugin:version'
    }
}

allprojects {
    apply plugin: 'ru.surfstudio.android.cofiguration'

    androidStandard.useLocal = true
    androidStandard.localPath = "absolute path to android-standard"

    repositories {
        maven { url 'https://artifactory.surfstudio.ru/artifactory/libs-release-local' }
    }
}
```

build.gradle(app)
```groovy

dependencies {
    implementationStandard "your dependencies"
    apiStandard "your dependencies"
}
```