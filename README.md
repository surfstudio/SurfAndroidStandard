# Surf Android Standard

[![Android Arsenal][android_arsenal_icon]][android_arsenal_link]
[![Build Status][build_status_icon]][build_status_link]
[![License][license_icon]][license_link]
[![Download][build_version_icon]][build_version_link]

Фреймворк для разработки android приложений [студии Surf](https://www.surf.ru/). 
Содержит модули и стандарты разработки, с помощью которых можно построить качественное приложение
в короткие сроки. 

**!!! Репозиторий в стадии активной разработки !!!**

**[Стандарты разработки приложений в Surf][docs]**

**[Список компонентов][components]**

[Telegram чат](https://t.me/surf_android)

## **Использование**

build.gradle(root)
```groovy
allprojects {
    repositories {
        maven { url 'https://artifactory.surfstudio.ru/artifactory/libs-release-local' }
    }
}
```

build.gradle(app)
```groovy
dependencies {
    implementation "ru.surfstudio.android:%ARTIFACT-ID-HERE%:%VERSION-HERE"
}
```

Актуальные версии можно посмотреть:
- в [бинтрей](https://bintray.com/surf/maven) для стабильных артефактов
- в [артифактори](https://artifactory.surfstudio.ru/artifactory/libs-release-local/ru/surfstudio/android/) для нестабильных артефактов

Список всех компонентов [здесь][components]. 

## License
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

[docs]: docs/main.md
[components]: docs/components.md

[build_status_link]: https://jenkins.surfstudio.ru/view/Projects/view/Android_Standard/job/Android_Standard_Dev_Deploy/lastBuild/
[build_status_icon]: https://jenkins.surfstudio.ru/buildStatus/icon?job=Android_Standard_Dev_Deploy/

[license_link]: http://www.apache.org/licenses/LICENSE-2.0
[license_icon]: https://img.shields.io/badge/license-Apache%202-blue

[android_arsenal_link]: https://android-arsenal.com/details/1/7290
[comment]: # (В следующих версиях нужно заменить на ссылку на иконку реального статуса)
[android_arsenal_icon]: https://img.shields.io/badge/Android%20Arsenal-SurfAndroidStandard-green.svg?style=flat

[build_version_link]: https://bintray.com/surf/maven
[comment]: # (В следующих версиях нужно заменить на ссылку на иконку реального статуса)
[build_version_icon]: https://img.shields.io/badge/jcenter-libs-brightgreen
 
