package ru.surfstudio.android.mvp.binding.rx.sample.react.event

import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.OpenScreen
import ru.surfstudio.android.core.mvp.binding.react.loadable.LoadableEvent
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList

class QueryChangedEvent(val query: String) : Event
class LoadListEvent : LoadableEvent<DataList<String>>()
class OpenProfileScreen : OpenScreen("profile")