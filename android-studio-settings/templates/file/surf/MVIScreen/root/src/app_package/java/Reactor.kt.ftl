package ${packageName}

import ru.surfstudio.android.core.mvi.ui.reactor.Reactor
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject
import ${packageName}.${eventClassName}.*

<#if isMergedStateHolder>
/**
 * StateHolder экрана [${viewClassName}].
 */
@PerScreen
class ${stateHolderClassName} @Inject constructor()
</#if>

/**
 * [Reactor] экрана [${viewClassName}].
 */
@PerScreen
class ${reactorClassName} @Inject constructor() : Reactor<${eventClassName}, ${stateHolderClassName}> {

    override fun react(sh: ${stateHolderClassName}, event: ${eventClassName}) {
        when (event) {
            /* do nothing */
        }
    }
}
