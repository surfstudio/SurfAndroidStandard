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
*Зачем:* выделить цветом используемые вью с помощью **kotl in.synthetic** для наглядности

*Как настроить:*  Settings -> Editor -> Color Scheme -> Kotlin -> Properties and Variables -> Android Extensions synthetic properties -> Foreign -> Цвет `#0AB9D5` (или по вкусу)

#### Оптимизация ОЗУ для Android Studio
*Зачем:* ускоряем наш главный инструмент

*Что сделать:*  

1. Settings -> Appereance&Behavior -> Appereance -> Show memory indicator (в нижнем правом углу видим кол-во потребляемой памяти студией, при клике очищается память по возможности)
2. Settings -> Compiler -> Compile independent modules in parallel (чтобы модули собирались одновременно)
3. Help -> Edit Custom VM Options ->  Создатся файл -> -Xmx4g (можно и 8g) ->  Перезапустить Android Studio

#### Плагины

1. [Android drawable preview](https://github.com/mistamek/Android-drawable-preview-plugin/blob/master/README.md). Превью в общем списке drawable (must have)
2. Markdown support. Для просмотра и редактирования `.md` файлов
3. [Key promoter X](https://github.com/halirutan/IntelliJ-Key-Promoter-X) учимся почти не использовать мышку
4. [JSON to Kotlin class](https://github.com/wuseal/JsonToKotlinClass). К слову таких плагинов довольно много, можно выбрать любой.
5. JSON Viewer. Просто форматирование JSON'а для наглядности. Так же есть огромное кол-во аналогов.
6. [CodeGlance](https://github.com/Vektah/CodeGlance). Миниатюра всего текста в файле справа (как в Sublime text)
7. [ADB idea](https://github.com/pbreault/adb-idea). Упрощение работы с ADB
9. Git Branch Cleaner - Очистка старых веток гита. Использование: VCS → Git → Delete Old Branches

#### Полезные ссылки

1. [Кастомная раскраска логгера](https://medium.com/@gun0912/android-studio-how-to-change-logcat-color-3c17a10beef8)
