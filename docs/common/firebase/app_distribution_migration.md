# Миграция проекта с fabric на firebase app distribution

После пошаговой миграции с fabric.io на firebase необходимо сделать следующее:

1. В **app/build.gradle** добавить строку
`apply plugin: 'com.google.firebase.appdistribution` до строки
`apply from: '../config.gradle'` и раскоментить
`apply plugin: 'com.google.gms.google-services'` в конце файла,
если ранее сделано не было.
1. Из консоли проекта в firebase загрузить файл **google-services.json**
и добавить его в модуль app.
1. В **build.gradle** уровня проекта добавить
```
buildscript {
    ...
    dependencies {
        ...
        classpath "com.google.firebase:firebase-appdistribution-gradle:$firebaseAppDistributionVersion"
    }
}
```
1. В **config.gradle** добавить актуальную версию
`firebaseAppDistributionVersion = '1.2.0'    //http://bit.ly/2Gc2qHh`
1. В **buildTypes.gradle** для билд тайпов qa и release (и других, для 
которых выкладываются сборки) добавить
```
firebaseAppDistribution {
    groups="TesterGroupName"
}
```
1. В файле **ci/JenkinsfileTagJob.groovy** после `pipeline.init()`
добавить `pipeline.useFirebaseDistribution = true`

[Документация](https://firebase.google.com/docs/app-distribution/android/distribute-gradle)