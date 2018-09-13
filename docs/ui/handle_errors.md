[Главная](../main.md)

[TOC]

# Обработка ошибок

Ошибки с запросов к репозиторию обрабатывеются на UI-слое в презентере.

Для этого предусмотрен механизм, с возможностью поставить стандартный обработчик
ошибок на все запросы. Он предоставляется модулем [core-mvp][mvp].

Частные случаи и установку состояния экрана при ошибке следует производить
в коллбеках Rx-цепочек.

Например, при использовании *core-mvp* и поставлемого с им базового презентера
обрабтка будет выглядить так:

```
stopCampaignDisposable = subscribeIoHandleError(postInteractor.stopPromoCampaign(screenModel.postId),
                {
                    view.showPromotionStopped()
                },
                {
                    screenModel.loadState = LoadState.ERROR
                    view.render(screenModel)
                })
```

