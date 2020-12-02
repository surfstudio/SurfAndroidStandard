Этот вид шаблонов совместим с библиотекой [Geminio](https://github.com/hhru/android-multimodule-plugin/tree/master/plugins/hh-geminio "Geminio").

Подробно с принципом ее работы можно ознакомиться в [статье](https://m.habr.com/ru/company/hh/blog/529948/http:// "статье")

Чтобы они заработали у вас в проекте, нужно выполнить следующие шаги:
1. Установите плагин Geminio из архива, доступного [тут](https://github.com/hhru/android-multimodule-plugin/blob/master/distr/hh-geminio.ziphttp:// "тут")
2. Скопируйте внутрь проекта папку geminio, содержащую все шаблоны

3. Внутри своего проекта создайте файл geminio_config.yaml со следующим содержимым:
```
templatesRootDirPath: /geminio/templates
groupsNames:
	   forNewGroup: Surf Templates
```
	*   **templatesRootDirPath** - это относительный путь от папки вашего проекта до папки с шаблонами, которую должен будет прочитать плагин Geminio
	*   **groupNames** - названия групп, в которые будут добавлены action-ы.
	*   **forNewGroup** - название группы, которая отобразится в меню New (CMD+N на Project View)

4. После создания файла, откройте страничку настроек **Preferences -> Appearance & Behavior -> Geminio plugin**. Там выберите путь до файла-конфига, нажмите Apply.

5. После этого - перезагружайте проект и ваши шаблоны должны подтянуться. При изменении шаблона, чтобы Android Studio увидела этот шаблон, придётся тоже перезагрузить проект.
