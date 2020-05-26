[Главная](../../main.md)

# Kotlin CodeStyle

- [Основные положения](#основные-положения)
- [Правила оформления комментариев](#правила-оформления-комментариев)
- [Именование сущностей](#именование-сущностей)
  - [Имена констант в xml:](#имена-констант-в-xml)
  - [Именование свойств](#именование-свойств)
  - [Именование методов](#именование-методов)
  - [Использование спец. символов](#использование-спец-символов)
- [Структура класса](#структура-класса)
- [Организация файлов и пакетов](#организация-файлов-и-пакетов)
- [Форматирование](#форматирование)
  - [Пустые/ не пустые блоки кода](#пустые-не-пустые-блоки-кода)
  - [Методы](#методы)
  - [Свойства](#свойства)
  - [Заголовок классов](#заголовок-классов)
  - [Форматирование управляющих конструкций](#форматирование-управляющих-конструкций)
  - [Форматирование цепных вызовов](#форматирование-цепных-вызовов)
  - [Форматирование лямбд](#форматирование-лямбд)
  - [Форматирование выражений](#форматирование-выражений)
- [Идиомы](#идиомы)
  - [Kotlin конструкции](#kotlin-конструкции)
- [Лучшие практики](#лучшие-практики)
- [Настройка форматирования для проекта](#настройка-форматирования-для-проекта)
  - [Локальное форматирование](#локальное-форматирование)
  - [Форматирование для CI](#форматирование-для-ci)


## Основные положения

Основано на [Coding Conventions][conv] и [Android Style Guide][style]

Перед *Pull Request* **ОБЯЗАТЕЛЬНО** делаем форматирование кода по `Ctrl+Alt+L`
(`⌘ + ⌥ + L`)

## Правила оформления комментариев

см. [Android Code Style][surf]

Оформляются по правилам `KDoc`.
(см. [Coding Conventions][conv], [Android Style Guide][style])

## Именование сущностей
см. [Coding Conventions][conv], [Android Code Style][surf]

**Указаны отличия от [Java код-стайла Surf][surf]**

### Имена констант в xml:

* screen_semantic_postfix, где

    * screen - имя экрана, на котором используется константа,
может отсутствовать, если используется на многих экранах (без префикса activity/fragment)

    * semantic - смысл

    * postfix - назначение:
        * _text строковая константа используется в TextView,
        * _btn строковая константа используется для Button
        * _message, _error_message - используется в Toast / Snack
        * _hint - подсказка в EditText
        * _padding, _margin - паддинг и марджин соответственно
        * _width, _height - ширина и высота соответственно

### Именование свойств

Используется `camelCase`. Тип свойства указывается **явно**.

Если используются свойства с функциональном типом, то делаем постфикс `Lambda`
```
val someBeautifulLambda: (Int) -> Int = { it * 2 }
```

Если такое свойство используется как *callback*, то вместо `Lambda` ->  `Callback`.

### Именование методов

Используется `camelCase`. Правила оформления смотри [Android Code Style][surf].

### Использование спец. символов

см. [Special Characters from Android Style Guide](https://android.github.io/kotlin-guides/style.html#special-characters)

## Структура класса

* companion object
* приватные поля-ключи(константы)
* приватные val / var свойства
* публичные val/ var свойства
* публичные интерфейсы
* методы
* вложенные классы

#### Расположение методов в классе

**1 вариант**:

* abstract методы

* override методы

* публичные методы

* internal методы

* protected методы

* приватные методы

**2 вариант**:

* по уровням абстракции (см.  [Android Code Style][surf])

## Организация файлов и пакетов

* Выносить extension-методы в отдельные, логически связанные файлы,
если они не используются только в одном месте. Файл именуется с
постфиксом Extensions, при этом префикс должен соответсвовать классу,
который эти методы расширяют,
т.е `EditTextExtensions`, `ListExtensions`, `ActivityExtensions` и тд.

* Публичные глобальные константы хранить как `val` свойства в отдельном файле,
на глобальном уровне.

* Утилитные функции удобно хранить либо в виде *глобальных функций*, либо
оборачивать в `object`.

## Форматирование

### Пустые/ не пустые блоки кода
см [Non-empty/Empty Blocks](https://android.github.io/kotlin-guides/style.html#non-empty-blocks)

### Методы

см. [Function formatting](https://kotlinlang.org/docs/reference/coding-conventions.html#function-formatting),
[Вызов методов](https://kotlinlang.org/docs/reference/coding-conventions.html#method-call-formatting)

*Замечание*: форматирование аргументов метода, когда не помещается
в одну строку. Делаем так:

```
override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
) {
   //some actions
}
```


Если выражение однострочного метода не помещается в одну строку, то переносить
его на следующую.
```
fun authByEmail(email: String, password: String): Observable<User> =
    authApi.auth(AuthRequest(email = email, password = password))
       .transform()
```

### Свойства
см. [Форматирование свойств](https://kotlinlang.org/docs/reference/coding-conventions.html#property-formatting)

### Заголовок классов
см. [Форматирование заголовка класса](https://kotlinlang.org/docs/reference/coding-conventions.html#class-header-formatting)

```
class SocialNetworksInteractor @Inject constructor(
    private val vkRepository: VkRepository,
    private val fbRepository: FbRepository,
    private val analyticsService: AnalyticsService,
    private val activityProvider: ActivityProvider
) : OAuthCallback, ActivityResultDelegate {

}
```

### Форматирование управляющих конструкций
см. [Форматирование управляющих конструкций](https://kotlinlang.org/docs/reference/coding-conventions.html#formatting-control-flow-statements)

### Форматирование цепных вызовов
см. [Цепные вызовы](https://kotlinlang.org/docs/reference/coding-conventions.html#chained-call-wrapping)

### Форматирование лямбд
см. [Форматирование лямбд](https://kotlinlang.org/docs/reference/coding-conventions.html#lambda-formatting)

Если метод принимает только лямбду, сразу пишем ее после названия метода,
без круглых скобок

```
    doSomething { //do something }
```

Внутри лямбды удобно использовать `it`, если *не требуется* уточнения или
внутри *не используется* еще одна лямбда, требующая параметр внешней лямбды(см. 2)
1)
``` kotlin
someList.map { it.id }.toHashSet()

someList.map {
	it.map { it.name }
}
```
2)
```kotlin
someList.map { objInList ->
	// someAnotherList - коллекция вне лямбды
	someAnotherList.filter { it.id == objInList.id}
}
```

### Форматирование выражений

##### Подписки

В методах `subscribe` писать лямбды с новой строки для лучшей читаемости кода.

```
subscribe(
    activityNavigator.observeResult(CategoryChooserRoute::class.java)
        .filter { result -> result.isSuccess },
        { categoryChooserParam ->
            screenModel.categoryList = categoryChooserParam.data.categoryList
            view.render(screenModel)
    }
)
```

Плюс к этому в случае передачи в subscribe метода принимающего большое
количество аргументов лучше переносить вызов метода на следующую строку
```
subscribeIoHandleError(
    placesRepository.getShortPlaces(
        filter?.categories?.toList(),
        param,
        param
    ),
    {
         //some actions
    })
```
```
subscribeIoHandleError(placesRepository.getShortPlaces(
       filter?.categories?.toList(),
       param,
       filter?.isPromo ?: false,
       geoPosition = placesLoadRequestData.geodataRequest),
{})
```

**Примечание:** Если Observable большой, то выносить в переменную (или даже метод)
и там применять нужные операторы, а то метод может стать очень большим и страшным
```kotlin
    val someObservable = activityNavigator
            .observeResult(CategoryChooserRoute::class.java)
            .filter { result -> result.isSuccess }

    subscribe(
        someObservable,
        { categoryChooserParam ->
            screenModel.categoryList = categoryChooserParam.data.categoryList
            view.render(screenModel)
        },
        { t: Throwable -> handleError(t) }
    )

```

##### Логические выражения

В **логических выражениях** предпочтительнее использовать логические
операторы &, | и  тд, а не методы в явном виде. Данный тезис не касается `nullable` типов.
```
return isEmptyFirstName.not().and(isEmptyLastName.not()).and(isEmptyPhone.not())

return !isEmptyFirstName & !isEmptyLastName & !isEmptyPhone
```

Здесь нельзя написать `!screenModel.currentUserLocation?.isLocationDefault()`,
так как currentLocation может вернуть null
```
fun someBool() : Boolean = screenModel.currentUserLocation?.isLocationDefault()?.not() ?: false
```

## Идиомы

см. [Idiomatic use of language features](https://kotlinlang.org/docs/reference/coding-conventions.html#idiomatic-use-of-language-features)

### Kotlin конструкции

#### .apply{}

Удобно использовать, когда необходимо  проинициализировать свойства
объекта при передачи, как аргумента, в метод. Но надо быть внимательным
и по возможности не собирать все в одном месте.

#### .let{}

Удобно использовать как проверку на `null`. При этом, внутри блока let,
переменная будет гарантировано не null.
```kotlin
var nullableInt: Int? = null

nullableInt = 11

// if (nullableInt == 11) <- будет требовать
// выражения nullableInt?.equals(11) ?: false

nullableInt?.let {
    if (it == 11) //do something
}
```


## Лучшие практики

* Обращение к вью через kotlin.synthetic.* (через имена в xml в lower_snake_case).
*Искл*: в контроллерах удобнее использовать findViewById или Anko.find(),
либо инициализацию через lazy {}

* С помощью lazy удобно инициализировать большие объемные списки, объекты.

* Использовать синтаксис свойств вместо сеттеров.

* Для конкатенации элементов списка в строку(например с идентификаторами
или именами элементов) удобно использовать метод joinToString().

* [**Delegates**][delegates]

## Настройка форматирования для проекта

### Локальное форматирование

Настройка форматирования для AndroidStudio
Из-за размещения настроек под vcs изменения распространятся на всю команду.

1. Перенести содержимое [.idea][ideaCodeSettings] в корневую директорию 
.idea проекта
2. Добавить в корневой .gitignore  
`!.idea/codeStyles`  
`!.idea/inspectionProfiles`  
И заменить в нем  
`.idea` на `.idea/*`
3. `$git add .idea/`  
   `$git commit -a -m "Code formatting”`
4. Убрать из `gradle.properies` `kotlin.code.style={official}` (если имеется)
5. Запушить изменения

### Форматирование для CI
#### Установка
1. Загрузить [ktFormatter.gradle][ktFormatter] в директорию {project}/scripts
рабочего проекта.
2. Добавить
`ktlintPluginVersion = '2.1.0'               //https://bit.ly/2YLc3n8`  
в .gradle файл содержащий версии зависимостей. Как правило это {project}/config.gradle  
Имеет смысл проверить и при возможности обновить версию плагина.
3. Добавить   
`apply from: "scripts/ktFormatter.gradle"`  
в корневой build.gradle проекта.
4. Добавить параметр в gradle.properties (если нет)  
`surf_maven_libs_url=https\://artifactory.surfstudio.ru/artifactory/libs-release-local`
5. Проверить работоспособность командой  
`./gradlew ktlintFilesFormat -PlintFiles=relative_path_to_file`    
где relative_path_to_file это относительный путь к файлу с заранее испорченным форматированием.
6. Запушить изменение в основную ветку спринта. Соответственно, автоформатирование будет работать только в тех ветках, которые содержат коммит с изменениями выше
7. В master ветке проекта в файле `ci/JenkinsfilePullRequestJob.groovy` добавить следующие строки в любое место между `def pipeline = new PrPipelineAndroid(this)` и `pipeline.run()`  
```
pipeline.getStage(pipeline.CODE_STYLE_FORMATTING).strategy = StageStrategy.UNSTABLE_WHEN_STAGE_ERROR
pipeline.getStage(pipeline.UPDATE_CURRENT_COMMIT_HASH_AFTER_FORMAT).strategy = StageStrategy.UNSTABLE_WHEN_STAGE_ERROR
```  
И запушить изменения

Для проверки можно использовать следующий [коммит](https://bitbucket.org/surfstudio/unicredit-android/commits/5b0ef710aa7387123d650378b6aefa0261ba5870#chg-config.gradle) 
с полной установкой форматирования.

#### Настройка для CI
Можно изменять список правил для форматирования добавляя свои или удаляя существующие    
Для отключения любого из них достаточно добавить
```
task ktlintFilesFormat(type: org.jmailen.gradle.kotlinter.tasks.FormatTask) {
...
disabledRules = ["{имя_правила}"]
...
}
```
{имя_правила} можно получить из   
[Списка существующих правил](https://github.com/pinterest/ktlint/tree/master/ktlint-ruleset-standard/src/main/kotlin/com/pinterest/ktlint/ruleset/standard)  
в классе правила в строке декларирования класса `: Rule("имя_правила")` 


[conv]: https://kotlinlang.org/docs/reference/coding-conventions.html
[style]: https://android.github.io/kotlin-guides/style.html
[robot]: https://habrahabr.ru/company/redmadrobot/blog/343458/
[surf]: java_codestyle.md
[r_naming]: https://github.com/RedMadRobot/kotlin-style-guide#%D0%9F%D1%80%D0%B0%D0%B2%D0%B8%D0%BB%D0%B0-%D0%B8%D0%BC%D0%B5%D0%BD%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F
[special_chars]: https://android.github.io/kotlin-guides/style.html#special-characters
[delegates]: kotlin_delegates.md
[ktFormatter]: https://bitbucket.org/surfstudio/android-standard/downloads/ktFormatter.gradle
[ideaCodeSettings]: https://bitbucket.org/surfstudio/android-standard/downloads/ideaProjectConfig.zip


