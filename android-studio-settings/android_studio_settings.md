[Главная](../main.md)

# Настройки и плагины для Android Studio

#### Tools&Contexts
*Зачем*: удобное связывание задач в Jira и веток в git за пару кликов

*Как настроить*:

1. Tools -> Tasks&Contexts -> Configure Servers -> Add -> Заполняем:
    - Server URL: `https://jira.surfstudio.ru`
    - Логин и пароль от Jira
    - Search: `assignee = currentUser() order by updated`. Также можно использовать фильтры, 
        которые настроили в Jira. Например: `filter = anddep-user`
        
    - нажимаем **Test** для проверки
    
2. На вкладке **Commit Message** ставим галочку и выставляем `{id}`
3. Settings -> Tools > Tasks (в зависимости от вашего git flow)
    - Changelist name format: `{id} {summary}`
    - Feature branch name format: `{id} {summary}`

*Как использовать:* Tools -> Tasks&Contexts - создаем задачу, автоматически создатся ветка с подходящим названием, при переключении между задачами переключается соответствующая ветка с changelist'ом

#### Выделяем цветом kotlin.synthetic
*Зачем:* выделить цветом используемые вью с помощью **kotlin.synthetic** для наглядности

*Как настроить:*  Settings -> Editor -> Color Scheme -> Kotlin -> Properties and Variables -> Android Extensions synthetic properties -> Foreign -> Цвет `#0AB9D5` (или по вкусу)

#### Оптимизация ОЗУ для Android Studio
*Зачем:* ускоряем наш главный инструмент

*Что сделать:*  

1. Settings -> Appereance&Behavior -> Appereance -> Show memory indicator (в нижнем правом углу видим кол-во потребляемой памяти студией, при клике очищается память по возможности)
2. Settings -> Compiler -> Compile independent modules in parallel (чтобы модули собирались одновременно)
3. Help -> Edit Custom VM Options ->  Создатся файл -> -Xmx4g (можно и 8g) ->  Перезапустить Android Studio

#### Оптимальная производительность при работе с Android Studio на Ubuntu 18.04 LTS.

Даже при достаточной производительности ПК возможна следующая ситуация: потребление памяти процессами AS, "Gradle Daemon" и "Kotlin Compile Daemon" не ограничено должным образом, 
из-за этого остальные процессы вытесняются из RAM на в десятки раз более медленный накопитель в swap, вследствие чего начинаются тормоза при использовании параллельно другого ПО, также swap может быть и исчерпан. 

В RAM должно оставаться достаточное место для другого ПО (порядка 6-8ГБ). 

Приоритет стоит отдать больше выполнению GC, чем своппингу, т.к. своппинг в несколько ГБ это 100% более жесткие тормоза.

***Для Ubuntu 18.04 LTS можно предпринять следующее:***

- **Настроить работу Ubuntu со swap для уменьшения возможного своппинга:**
```bash
echo 'vm.swappiness=10' | sudo tee -a /etc/sysctl.conf
```
*[Источник](https://help.ubuntu.ru/wiki/ubuntu_optimization)*

- **Увеличить размер файла подкачки до 16ГБ:**
```bash
sudo swapoff /swapfile && \
sudo rm /swapfile && \
sudo fallocate -l 16G /swapfile && \
sudo chmod 600 /swapfile && \
sudo mkswap /swapfile && \
sudo swapon /swapfile
```
*[Источник](https://www.digitalocean.com/community/tutorials/how-to-add-swap-space-on-ubuntu-18-04)*

- **Настроить JDK для AS:**

По замерам AS обычно потребляет порядка 700-1500 МБ.

Открыть файл с настройками JDK в AS (Help -> Edit Custom VM Options) и вставить следующие строки (при необходимости можно увеличить до 3 ГБ):
```gradle
-Xmx2g
-XX:ReservedCodeCacheSize=512m
```

*Источники - Android Studio is slow (how to speed up)?, Комфортная работа с Android Studio*

- **Ограничить использование Java Heap через конфигурацию gradle.properties в проекте (2-4 ГБ в зависимости от величины проекта):**
```gradle
org.gradle.jvmargs=-Xmx3g
```

**УКАЗАННЫЕ ПАРАМЕТРЫ С ТЕЧЕНИЕМ ВРЕМЕНИ МОГУТ ТРЕБОВАТЬ КОРРЕКТИРОВКИ**

#### Плагины

1. [Android drawable preview](https://github.com/mistamek/Android-drawable-preview-plugin/blob/master/README.md). Превью в общем списке drawable (must have)
2. Markdown support. Для просмотра и редактирования `.md` файлов
3. [Key promoter X](https://github.com/halirutan/IntelliJ-Key-Promoter-X) учимся почти не использовать мышку
4. [JSON to Kotlin class](https://github.com/wuseal/JsonToKotlinClass). К слову таких плагинов довольно много, можно выбрать любой.
5. JSON Viewer. Просто форматирование JSON'а для наглядности. Так же есть огромное кол-во аналогов.
6. [CodeGlance](https://github.com/Vektah/CodeGlance). Миниатюра всего текста в файле справа (как в Sublime text)
7. [ADB idea](https://github.com/pbreault/adb-idea). Упрощение работы с ADB
9. Git Branch Cleaner - Очистка старых веток гита. Использование: VCS → Git → Delete Old Branches
10. [Bitbucket Linky](https://plugins.jetbrains.com/plugin/8015-bitbucket-linky) - создание PR по Ctrl+Shift+X,P из студии. (Предварительно нужно настроить в Other Settings.)
11. [CPU Usage Indicator](https://plugins.jetbrains.com/plugin/8580-cpu-usage-indicator) - Позволяет включить индикатор использования процессора. Также позволяет смотреть дамп тредов и фейлов.
12. [Vector Drawable Thumbnails](https://plugins.jetbrains.com/plugin/10741-vector-drawable-thumbnails) - Просмотр drawable в виде сетки, в отдельном окошке.
13. [Rainbow Brackets](https://plugins.jetbrains.com/plugin/10080-rainbow-brackets) - Раскрашивает открывающую и закрывающую скобку в один цвет, чтобы удобно было читать вложенные конструкции
14. [Hunspell](https://plugins.jetbrains.com/plugin/10275-hunspell) - Продвинутый спелчекер. [Добавление русского языка](#hunspell)

##### Настройка плагинов

###### Hunspell
1. В Ide: Settings -> Plugins -> Находим - ставим плагин Hunspell
2. Перезагружаем ide
3. Качаем словарь.
    * Linux: 
        1. `cd {путь где будет лежать словарь}` 
        2. `$npm install dictionary-ru` ( если нет npm: `$sudo apt install npm`)
    * Или вручную https://github.com/wooorm/dictionaries/tree/master/dictionaries/ru
4. В Ide: Settings -> Editor -> Spelling -> Dictionaries -> “+” -> выбираем скачанный файл *.dic (`.../node_modules/dictionary-ru/index.dic`)
Словари применяются только к проекту, локально для каждого пользователя. Поэтому, операцию стоит применить к каждому существующему проекту.
Также, можно закрыть все открытые проекты и открыть settings на приветственном экране и добавить словарь.
В этом случае, словарь будет добавляться во все новые и импортируемые проекты

#### Полезные ссылки

1. [Кастомная раскраска логгера](https://medium.com/@gun0912/android-studio-how-to-change-logcat-color-3c17a10beef8)
