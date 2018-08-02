# Подключение репозитория

build.gradle(root)
```groovy
allprojects {
    repositories {
        maven { url "http://artifactory.surfstudio.ru/artifactory/libs-release-local" }
    }
}
```
build.gradle(app)
```groovy
dependencies {
    implementation "ru.surfstudio.android:artifact-id:version"
}
```
Актуальные версии [здесь](https://bitbucket.org/surfstudio/android-standard/wiki/Home).