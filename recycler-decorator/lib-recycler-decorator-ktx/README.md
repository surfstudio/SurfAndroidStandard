[Главная страница репозитория](/docs/main.md)

[TOC]

# RecyclerView decorator ktx

Расширения для подключения EasyAdapter к Decorator.Builder

Расширения дают возможность создать только
ViewHolderDecor, OffsetDecor и привязать к определенному viewType

Для реализации декоратора для ViewHolder неоходимо наследоваться от BaseViewHolderDecor.

Для реализации оффеста для ViewHolder неоходимо наследоваться от BaseViewHolderOffset

Подключение декораторов происходит так же методами билдера

.underlay(...)

.overlay(...)

.offset(...)

Пример можно посмотреть тут
```
ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat
```

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:recycler-decorator-ktx:X.X.X"
```