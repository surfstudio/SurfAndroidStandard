package ru.surfstudio.android.mvp.binding.rx.sample.react.event

import ru.surfstudio.android.core.mvp.binding.react.Event
import ru.surfstudio.android.core.mvp.binding.react.NetworkRequestEvent

class LoadNextPageEvent
class QueryChangedEvent(val query: String) : Event
class ReloadListEvent

class LoadListNetworkRequest : NetworkRequestEvent<List<String>>()