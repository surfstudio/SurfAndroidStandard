# Android-Standard version plugin

There are two types of versions:
- main version of all repository
- version of specific library/module

This plugin provide specific library version from Android-Standard 
based on main Android-Standard version by library name

### Usage

build.gradle(root)
```groovy
buildscript {
    repositories {
        
        maven { url 'https://artifactory.surfstudio.ru/artifactory/libs-release-local' }
    }

    dependencies {
        classpath 'ru.surfstudio.android:version-plugin:version'
    }
}
```

build.gradle(app)
```groovy
apply plugin: 'ru.surfstudio.android'

dependencies {
    implementation "ru.surfstudio.android:artifact-id:${androidStandard.version("artifact-id")}"
}
```

### How to deploy/update this plugin? (for internal usage)

1. run task **generateDataForPlugin** that will generate json with versions 
inside plugin module

2. update/deploy ***android-standard-version-plugin*** by running task 
**updateArchives** from plugin module