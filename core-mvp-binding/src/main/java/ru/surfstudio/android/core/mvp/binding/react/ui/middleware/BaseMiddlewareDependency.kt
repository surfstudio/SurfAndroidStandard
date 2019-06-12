package ru.surfstudio.android.core.mvp.binding.react.ui.middleware

import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency

class BaseMiddlewareDependency(
        val basePresenterDependency: BasePresenterDependency,
        val hub: RxEventHub
)