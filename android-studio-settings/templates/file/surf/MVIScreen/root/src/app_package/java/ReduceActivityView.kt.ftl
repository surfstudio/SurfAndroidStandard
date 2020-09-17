package ${packageName}

import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.ui.view.${reduceViewParentClassName}
import ${packageName}.di.${configuratorClassName}
<#if applicationPackage??>
import ${applicationPackage}.R
</#if>
import javax.inject.Inject

internal class ${viewClassName} : ${reduceViewParentClassName}<${stateClassName}, ${eventClassName}>() {

    @Inject
    override lateinit var hub: ScreenEventHub<${eventClassName}>

    @Inject
    override lateinit var sh: ${stateHolderClassName}

    override fun getScreenName() = "${viewClassName}"

    override fun createConfigurator() = ${configuratorClassName}(intent)

    override fun getContentView(): Int = R.layout.${layoutName}

    override fun render(state: ${stateClassName}) {
        TODO("Not yet implemented")
    }

    override fun initViews() {
        TODO("Not yet implemented")
    }
}
