package ${packageName}

import ru.surfstudio.android.core.mvi.ui.reactor.Reactor
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject
import ${packageName}.${eventClassName}.*

<#if isMergedStateHolder>
@PerScreen
internal class ${stateHolderClassName} @Inject constructor()
</#if>

@PerScreen
internal class ${reactorClassName} @Inject constructor() : Reactor<${eventClassName}, ${stateHolderClassName}> {

    override fun react(sh: ${stateHolderClassName}, event: ${eventClassName}) {
        when (event) {
            /* do nothing */
        }
    }
}
