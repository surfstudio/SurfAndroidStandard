package ${packageName}

import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
<#if isMergedStateHolder>
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
</#if>
import javax.inject.Inject
import ${packageName}.${eventClassName}.*

<#if isMergedStateHolder>
//TODO обязательно добавить ключевое слово "data" и хотя бы одно поле. Это необходимо для корректного вычисления диффа между стейтами.
internal class ${stateClassName}

@PerScreen
internal class ${stateHolderClassName} @Inject constructor() : State<${stateClassName}>(${stateClassName}())
</#if>

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
