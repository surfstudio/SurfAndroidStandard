# Sample Common
Модуль, используемый для создания примеров к другим модулям,
которым не требуется конфигурация Dagger.

Содержит общие ресурсы для всех примеров.

# Подключение
Пример зависимостей для build.gradle примера, использующего данный модуль:

```
dependencies {
    //module-name - имя модуля, для которого создается пример
    implementation project(':module-name')
    implementation project(':sample-common')

    //other dependencies
}
```
