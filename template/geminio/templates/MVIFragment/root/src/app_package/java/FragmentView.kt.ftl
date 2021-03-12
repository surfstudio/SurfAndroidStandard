package ${packageName}

import android.os.Bundle
import androidx.core.os.bundleOf
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.surfstudio.android.core.mvi.event.hub.owner.SingleHubOwner
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
<#if (reduce)>
<#if applicationPackage??>
import ${applicationPackage}.ui.mvi.view.BaseRx${screenType?cap_first}View
</#if>
</#if>
<#if (react)>
import ru.surfstudio.android.core.mvi.ui.BaseReactFragmentView
</#if>
import ${packageName}.di.${configuratorClassName}
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
<#if applicationPackage??>
import ${packageName}.R
</#if>
import javax.inject.Inject

/**
 * Экран TODO
 */
internal class ${viewClassName} :<#if (reduce)> BaseMviFragmentView<${stateClassName}, ${eventClassName}>() <#else> BaseReactFragmentView()</#if>,
    CrossFeatureFragment,
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

    override fun createConfigurator() = ${configuratorClassName}(arguments ?: bundleOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.${layoutName}, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
    <#if ((reduce))>
        super.onActivityCreated(savedInstanceState, viewRecreated)
    </#if>
    <#if (react)>
        initViews()
        bind()
    </#if>
    }
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
