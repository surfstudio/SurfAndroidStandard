package ${packageName}

import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject
import ${packageName}.${eventClassName}.*

<#if isMergedStateHolder>
class ${stateClassName}

/**
 * StateHolder экрана [${viewClassName}].
 */
@PerScreen
class ${stateHolderClassName} @Inject constructor() : State<${stateClassName}>(${stateClassName}())
</#if>

/**
 * Reducer экрана [${viewClassName}].
 */
@PerScreen
class ${reducerClassName} @Inject constructor(
    dependency: BaseReactorDependency
) : BaseReducer<${eventClassName}, ${stateClassName}>(dependency) {

    override fun reduce(state: ${stateClassName}, event: ${eventClassName}): ${stateClassName} {
        return when (event) {
            else -> state
        }
    }
}
