package ru.surfstudio.standard.base_ui.component.provider

import android.content.Intent
import android.os.Bundle
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator
import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator
import ru.surfstudio.android.mvp.widget.configurator.BaseWidgetViewConfigurator
import kotlin.reflect.KClass


object ComponentProvider {

    lateinit var createActivityScreenConfigurator:
            (intent: Intent, kclass: KClass<*>) -> BaseActivityViewConfigurator<*, *, *>

    lateinit var createActivityConfigurator:
            (intent: Intent, kclass: KClass<*>) -> BaseActivityConfigurator<*, *>

    lateinit var createFragmentScreenConfigurator:
            (bundle: Bundle, kclass: KClass<*>) -> BaseFragmentViewConfigurator<*, *>

    lateinit var createDialogScreenConfigurator:
            (bundle: Bundle, kclass: KClass<*>) -> BaseFragmentViewConfigurator<*, *>

    lateinit var createWidgetScreenConfigurator:
            (kclass: KClass<*>) -> BaseWidgetViewConfigurator<*, *>
}