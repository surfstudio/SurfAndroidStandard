package ru.surfstudio.android.core.ui.sample.ui.screen.message

import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.ui.sample.R
import ru.surfstudio.android.message.MessageController
import javax.inject.Inject

class MessageActivityView: BaseRenderableActivityView<MessageScreenModel>() {

    @Inject
    internal lateinit var messageController: MessageController

    @Inject
    internal lateinit var messagePresenter: MessagePresenter

    override fun createConfigurator(): BaseActivityViewConfigurator<*, *, *> = MessageScreenConfigurator(intent)

    override fun getScreenName() = "MessageActivityView"

    override fun getContentView(): Int = R.layout.activity_main

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(messagePresenter)

    override fun renderInternal(sm: MessageScreenModel) {
        sm.snackParams?.let {
            messageController.show(it)
        }
        sm.toastParams?.let {
            messageController.showToast(it)
        }
    }
}