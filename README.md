# Surf Android Standard

Фреймворк для разработки android приложений [студии Surf](http://www.surf.ru/).
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
