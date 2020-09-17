package ${packageName}

import android.os.Bundle
import androidx.core.os.bundleOf
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun createConfigurator() = ${configuratorClassName}(arguments ?: bundleOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.${layoutName}, container, false)

    override fun render(state: ${stateClassName}) {
        TODO("Not yet implemented")
    }

    override fun initViews() {
        TODO("Not yet implemented")
    }
}
