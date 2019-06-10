package ru.surfstudio.android.mvp.binding.rx.sample.react

import ru.surfstudio.android.core.mvp.binding.rx.ui.BindModel
import ru.surfstudio.android.mvp.binding.rx.sample.react.reducer.QueryReducer
import ru.surfstudio.android.mvp.binding.rx.sample.react.reducer.ListReducer

class ReactiveBindModel : BindModel {
    val listReducer = ListReducer()
    val queryReducer = QueryReducer()
}

