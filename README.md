# Surf Android Standard

[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-SurfAndroidStandard-green.svg?style=flat )]( https://android-arsenal.com/details/1/7290 )
[![Download][build_version_icon]][build_version_link]
[![Build Status][build_status_icon]][build_status_link]
[![License][license_icon]][license_link]
[![Android Standard][android_standard_icon]][android_standard_link]

Фреймворк для разработки android приложений [студии Surf](http://www.surfstudio.ru/).
Содержит модули и стандарты разработки, с помощью которых можно построить качественное приложение
в короткие сроки.

**!!! Репозиторий в стадии активной разработки !!!**

**[Стандарты разработки приложений в Surf][docs]**

**[Список артефактов][artifacts]**

**[Release notes](RELEASE_NOTES.md)**

[Telegram чат](https://t.me/surf_android)

## **Использование**

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

Аткуальная стабильная версия: **0.3.0**

Актуальная нестабильная версия: **0.4.0-SNAPSHOT**

Список всех артефактов [здесь][artifacts].

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
[artifacts]: docs/artifacts.md

[build_version_link]: https://bintray.com/surf/maven/easyadapter/_latestVersion
[build_version_icon]: https://img.shields.io/bintray/v/surf/maven/easyadapter?label=JCenter
[build_status_link]: https://jenkins.surfstudio.ru/view/Projects/view/Android_Standard/job/Android_Standard_Component_Mirroring_Job/
[build_status_icon]: https://jenkins.surfstudio.ru/buildStatus/icon?job=Android_Standard_Component_Mirroring_Job
[license_link]: http://www.apache.org/licenses/LICENSE-2.0
[license_icon]: https://img.shields.io/badge/license-Apache%202-blue
[android_standard_icon]: https://img.shields.io/badge/Android%20Standard-Repo-brightgreen 
[android_standard_link]: https://github.com/surfstudio/SurfAndroidStandard