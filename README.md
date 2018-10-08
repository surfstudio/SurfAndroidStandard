# Surf Android Standard

Репозиторий с внутренними библиотеками для android проектов [surf](http://www.surfstudio.ru/).

**!!! Репозиторий в стадии активной разработки !!!**

* **[Документация по репозиторию][docs]**

* **[Список артефактов](https://bitbucket.org/surfstudio/android-standard/wiki/Modules)**

* **[Release notes](https://bitbucket.org/surfstudio/android-standard/wiki/Release%20notes)**

* **[Wiki в Bitbucket](https://bitbucket.org/surfstudio/android-standard/wiki/)**

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

Аткуальная стабильная версия: **0.2.1**

Актуальная нестабильная версия: **0.3.0-SNAPSHOT**

Список всех артефактов [здесь](https://bitbucket.org/surfstudio/android-standard/wiki/Modules).

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