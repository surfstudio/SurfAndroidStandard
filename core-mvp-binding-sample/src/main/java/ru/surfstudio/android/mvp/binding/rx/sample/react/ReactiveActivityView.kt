package ru.surfstudio.android.mvp.binding.rx.sample.react

import android.os.Bundle
import android.os.PersistableBundle
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_reactive_list.*
import ru.surfstudio.android.core.mvp.binding.react.Event
import ru.surfstudio.android.core.mvp.binding.react.EventHub
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.QueryChangedEvent
import javax.inject.Inject

class ReactiveActivityView : BaseRxActivityView() {

    override fun createConfigurator() = TODO()

    override fun getScreenName() = "ReactiveActivityView"

    override fun getContentView(): Int = TODO()

    @Inject
    lateinit var bm: ReactiveBindModel

    @Inject
    lateinit var hub: EventHub

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        reactive_query_tv.textChanges().map { QueryChangedEvent(it.toString()) }.sendTo(hub)

    }

    fun <T : Event> Observable<T>.sendTo(hub: EventHub) =
            hub.acceptEvent(this)
}