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
	url "${artifactory_context_url}"
    credentials {
    username = "${artifactory_user}"
    password = "${artifactory_password}"
    }
}
```
## gradle.properties
```properties
artifactory_context_url = http://artifactory.surfstudio.ru/artifactory/ext-release-local
artifactory_user = build
artifactory_password = AP5oyEgS8WyzJ37itfGvKvUSxdgFA8KGvyM9WJ
```


