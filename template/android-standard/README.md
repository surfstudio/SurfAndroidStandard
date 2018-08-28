#Механизм локальной загрузки модулей AndroidStandard

Для использования необходимо:

1. Скопировать данную папку android-standard с необходимыми скриптами в корень проекта;
2. Добавить в папку android-standard файл ```androidStandard.properties```
и указать в нем свойства ```androidStandardDebugDir=/full/path/to/your/local/android-standard```
и ```androidStandardDebugMode=true``` для активации режима локальной загрузки репозитория android-standard;
3. Добавить файл ```androidStandard.properties``` в ```gitignore``` проекта следующим образом: ```/android-standard/androidStandard.properties```.

**Важно**: после каждого изменения параметра ```androidStandardDebugMode``` для успешной сборки проекта необходимо выполнить ```Build - Clean Project```.