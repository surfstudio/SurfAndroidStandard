# Деплой в репозиторий артефактов
## Core
```
./gradlew clean core:assemble core:artifactoryPublish
```
# Импорт атефактов
## build.gradle(app)
```
implementation 'ru.surfstudio.android:{$artifactId}:{$artVersion}'
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


