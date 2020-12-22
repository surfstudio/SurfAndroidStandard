package ${packageName}

import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import javax.inject.Inject
import ${packageName}.${eventClassName}.*

/**
 * Reducer экрана [${viewClassName}]
 */
@PerScreen
internal class ${reducerClassName} @Inject constructor(
    dependency: BaseReactorDependency
) : BaseReducer<${eventClassName}, ${stateClassName}>(dependency) {

    override fun reduce(state: ${stateClassName}, event: ${eventClassName}): ${stateClassName} {
        return when (event) {
            else -> state
        }
    }
}
