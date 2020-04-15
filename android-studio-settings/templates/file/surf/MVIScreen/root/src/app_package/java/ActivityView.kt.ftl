package ${packageName}

import android.os.Bundle
import android.os.PersistableBundle
import ru.surfstudio.android.core.mvi.event.hub.owner.SingleHubOwner
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.ui.${viewParentClassName}
import ${packageName}.di.${configuratorClassName}
<#if applicationPackage??>
import ${applicationPackage}.R
</#if>
import javax.inject.Inject

/**
 * TODO
 */
class ${viewClassName} : ${viewParentClassName}(),
    SingleHubOwner<${eventClassName}> {

    @Inject
    override lateinit var hub: ScreenEventHub<${eventClassName}>

    @Inject
    lateinit var sh: ${stateHolderClassName}

    override fun getScreenName() = "${viewClassName}"

    override fun createConfigurator() = ${configuratorClassName}(intent)

    override fun getContentView(): Int = R.layout.${layoutName}

    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
        viewRecreated: Boolean
    ) {
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
