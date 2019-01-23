package ru.surfstudio.standard.base_ui.notification

import ru.surfstudio.android.core.mvp.activity.CoreActivityView
import ru.surfstudio.android.template.base_ui.R
import ru.surfstudio.standard.base_ui.provider.component.ComponentProvider
import javax.inject.Inject

class PushHandlerActivityView : CoreActivityView() {

    @Inject
    lateinit var presenter: PushHandlerPresenter

    override fun getPresenters() = arrayOf(presenter)

    override fun getScreenName() = "PushHandlerActivityView"

    override fun createConfigurator() = ComponentProvider.createActivityScreenConfigurator(intent, this::class)

    override fun getContentView() = R.layout.activity_transparent
}