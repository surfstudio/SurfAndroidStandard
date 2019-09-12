# Surf Android Standard

**Android-standard** - это фреймворк для разработки android приложений [студии Surf](http://www.surfstudio.ru/). Содержит множество компонент(**component**) и стандарты разработки, с помощью которых можно построить качественное приложение в короткие сроки.  
Компоненты - это совокупность gradle-модулей: библиотек (**lib**) и примеров их использования (**sample**).  
Все компоненты, их библиотеки и примеры конфигурируются в отдельном файле: [components.json](https://bitbucket.org/surfstudio/android-standard/src/98f32ea6fb290266013ea996d9b67787eb65abbd/buildSrc/components.json) (не build.gradle).  
Все константы, которые нужны для конфигурации, определяются в файле [config.gradle](https://bitbucket.org/surfstudio/android-standard/src/98f32ea6fb290266013ea996d9b67787eb65abbd/buildSrc/config.gradle). В том числе и версии сторонних библиотек. 

**!!! Репозиторий в стадии активной разработки !!!**

## Версии
Версии в Android-Standard формуруются по определенному правилу и могут быть четырех видов:  
1. 0.0.0 - стабильная, непроектная версия  
2. 0.0.0-alpha.unstable_version - нестабильная, непроектная версия (0.5.0-alpha.4)   
3. 0.0.0-projectPostfix.projectVersion - стабильная, проектная версия (0.5.0-mdk.4)   
4. 0.0.0-alpha.unstable_version-projectPostfix.projectVersion - нестабильная, проектная версия (0.5.0-alpha.4-mdk.4)  

Версии для компонент определяются в [components.json](https://bitbucket.org/surfstudio/android-standard/src/98f32ea6fb290266013ea996d9b67787eb65abbd/buildSrc/components.json). Библиотеки наследуют версию от компонент.  
Версия проекта (в том числе и проектная) определяется в [projectConfiguration.json](https://bitbucket.org/surfstudio/android-standard/src/ab87de178c1a1c63bde197c0aabcc3aa64a8b818/buildSrc/projectConfiguration.json).

Для использования snapshot артефактов необходимо добавить в  app/build.gradle (для мультимодульного проекта app-injector/build.gradle):

```groovy
configurations.all {
    resolutionStrategy.cacheDynamicVersionsFor 1, 'seconds'
    resolutionStrategy.cacheChangingModulesFor 1, 'seconds'
}
```
Промежуток времени установленный для хранения кэша важен для CI сборок. 
При сборке проекта будут использоваться последние snapshot версии зависимостей. Чтобы увидеть изменения в файлах нужно перезапустить Android Studio.

В крайнем случае, если изменения все-таки не загрузились, можно использовать:

1. в консоли - *./gradlew assembleDebug --refresh-dependencies*
2. на вкладке Gradle - "Refresh..."

Если после этих действий новая версия артефакта все равно не загружается, поможет удаление папки *.gradle* из директории проекта.

Подробнее про версии на странице [Workflow](https://bitbucket.org/surfstudio/android-standard/wiki/Workflow)
## Подключение

1. Подключение с помощью [плагина](https://bitbucket.org/surfstudio/android-standard/src/98f32ea6fb290266013ea996d9b67787eb65abbd/android-standard-version-plugin/README.md)  
2. [Локальное подключение](https://bitbucket.org/surfstudio/android-standard/src/ab87de178c1a1c63bde197c0aabcc3aa64a8b818/template/android-standard/README.md)

Аткуальная стабильная версия: **0.3.0**

Актуальная нестабильная версия: **0.4.0-SNAPSHOT**

Список всех артефактов [здесь](https://bitbucket.org/surfstudio/android-standard/wiki/Modules). 

## Ветки

1. **dev/0.0.0** - ветка для основной разработки
2. **release/component-name/0.0.0** - ветка для релиза компоненты 
3. **project-snapshot/project-name** - проектная ветка 

## **[Стандарты разработки приложений в Surf](https://bitbucket.org/surfstudio/android-standard/src/master/docs/main.md)**

## **[Source code](https://bitbucket.org/surfstudio/android-standard/src)**

## **[Список артефактов](https://bitbucket.org/surfstudio/android-standard/wiki/Modules)**

## **[Release notes](https://bitbucket.org/surfstudio/android-standard/src/master/RELEASE_NOTES.md)**

[Telegram чат](https://t.me/surf_android)

[Workflow в репозитории](https://bitbucket.org/surfstudio/android-standard/wiki/Workflow)

[Правила разработки модулей](https://bitbucket.org/surfstudio/android-standard/wiki/Rules)

[Правила релиза модулей](https://bitbucket.org/surfstudio/android-standard/wiki/ReleaseRules)

[Зеркало на GitHub](https://github.com/surfstudio/SurfAndroidStandard)

## **License**
```
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
```