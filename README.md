# Деплой в репозиторий артефактов
```
1. Поднимаем moduleVersionCode и moduleVersionName в файле config.gradle 
3. Ставим тег с соответствующей версией
3. Вополняем ./gradlew clean uploadArchives
4. Profit
```
# Импорт атефактов
## build.gradle(app)
```
implementation 'ru.surfstudio.android:{$artifactId}:{$moduleVersionName}'
```
## build.gradle(root)
```       
maven {
	url "${surf_maven_libs_url}"
    credentials {
    username = "${surf_maven_username}"
    password = "${surf_maven_password}"
    }
}
```
## gradle.properties
```properties
surf_maven_libs_url = http://artifactory.surfstudio.ru/artifactory/libs-release-local
surf_maven_username = build
surf_maven_password = AP5oyEgS8WyzJ37itfGvKvUSxdgFA8KGvyM9WJ
```


