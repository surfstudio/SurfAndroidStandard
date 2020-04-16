package ${packageName}

import android.os.Bundle
import androidx.core.os.bundleOf
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.surfstudio.android.core.mvi.event.hub.owner.SingleHubOwner
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.ui.${viewParentClassName}
import ${packageName}.di.${configuratorClassName}
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
<#if applicationPackage??>
import ${applicationPackage}.R
</#if>
import javax.inject.Inject

internal class ${viewClassName} : ${viewParentClassName}(),
    CrossFeatureFragment,
    SingleHubOwner<${eventClassName}> {

    @Inject
    override lateinit var hub: ScreenEventHub<${eventClassName}>

    @Inject
    lateinit var sh: ${stateHolderClassName}

    override fun getScreenName() = "${viewClassName}"

    override fun createConfigurator() = ${configuratorClassName}(arguments ?: bundleOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.${layoutName}, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        initViews()
        bind()
    }

    private fun initViews() {
        /* do nothing */
    }

    private fun bind() {
        /* do nothing */
    }
}
