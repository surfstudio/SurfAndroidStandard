#Custom view
Модуль с реализаций различных полезных кастомных виджетов.

##ЧТО НОВОГО

Здесь будет публиковаться перечень изменений при выходе новой версии модуля.

##СПИСОК ВИДЖЕТОВ

+ `TitleSubtitleView` - виджет для отображения текста с подписью;
+ `StandardPlaceHolderView` - стандартный полноэкранный плейсхолдер с поддержкой смены состояний.

##ПОДКЛЮЧЕНИЕ

Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru), необходимо, 
чтобы корневой `build.gradle` файл проекта был сконфигурирован так, как описано 
[здесь](https://bitbucket.org/surfstudio/android-standard/overview).
  
Для подключения модуля через `Gradle`:

    implementation "ru.surfstudio.standard:custom-view:X.X.X"
    
##ЗАВИСИМОСТИ И ТЕХНОЛОГИИ

Модуль имеет прямые зависимости от следующих модулей `Core`:

    :core-mvp           Модуль реализации дополненной MVP-архитектуры
    :util-ktx           Модуль с полезными extension-функциями
    :rx-extension       Модуль для работы с RxJava
    :animations         Модуль с анимациями
    
Модуль имеет прямые зависимости от следующих сторонних библиотек:

    me.zhanghai.android.materialprogressbar:library:1.4.2    Улучшенная версия ProgressBar

Модуль написан на `Kotlin`.

##ДОКУМЕНТАЦИЯ

+ [`StandardPlaceHolderView`](/custom-view/STANDARD-PLACEHOLDER-VIEW-README.md);