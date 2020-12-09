package ${packageName}

import android.os.Bundle
import android.os.PersistableBundle
import ru.surfstudio.android.core.mvi.event.hub.owner.SingleHubOwner
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
<#if (reduce)>
<#if applicationPackage??>
import ${applicationPackage}.ui.mvi.view.BaseMviActivityView
</#if>
</#if>
<#if (react)>
import ru.surfstudio.android.core.mvi.ui.BaseReactActivityView
</#if>
import ${packageName}.di.${configuratorClassName}
<#if applicationPackage??>
import ${applicationPackage}.R
</#if>
import javax.inject.Inject

// todo: добавить описание активити в манифест
/**
 * Экран TODO
 */
internal class ${viewClassName} : <#if (reduce)> BaseMviActivityView<${stateClassName}, ${eventClassName}>() <#else> BaseReactActivityView()</#if>,
    SingleHubOwner<${eventClassName}> {

    @Inject
    override lateinit var hub: ScreenEventHub<${eventClassName}>

    @Inject
    <#if (react)>
    lateinit var sh: ${stateHolderClassName}
    </#if>
    <#if (reduce)>
    override lateinit var sh: ${stateHolderClassName}
    </#if>

    override fun getScreenName() = "${viewClassName}"

    override fun createConfigurator() = ${configuratorClassName}(intent)

    override fun getContentView(): Int = R.layout.${layoutName}

<#if (react)>
    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
        viewRecreated: Boolean
    ) {
        initViews()
        bind()
    }
</#if>

<#if (reduce)>
    override fun initViews() {
        /* do nothing */
    }

    override fun render(state: ${stateClassName}) {
        /* do nothing */
    }
</#if>

<#if (react)>
    private fun bind() {
        /* do nothing */
    }
    private fun initViews() {
        /* do nothing */
    }
</#if>
}
