[Главная](../docs/main.md)

[TOC]

# Multimodule Template

Пример мультимодульного приложения использующего архитектуру приложения студии.
Служит для инициализации новых приложений.

### Инициализация проекта

В студии используется многомодульная архитектура проектов.
В этом модуле содержится минимальная конфигурация приложения.

О принципах и правилах построения многомодульной архитектуры можно
прочитать [здесь](../docs/common/multimodule/detail.md).

Чтобы начать работу скопируйте модуль и сделайте следующее:

    1. уберите строчки `null, //todo remove for real app
                     "LOCAL"  //todo remove for real app` из base/build.gradle
    2. в config.gradle `templatePrefix=':template'` измените на `templatePrefix=''`
    3. Если проект не собирается и выходит сообщение "Indexing freeze with message: Indexing paused due to batch updated" то вам [сюда](https://stackoverflow.com/questions/52513097/indexing-freeze-with-message-indexing-paused-due-to-batch-updated)
    4. исправьте все остальные todo