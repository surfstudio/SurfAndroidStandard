package ${packageName}

import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject
<#if (mviType=='reduce')>
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
</#if>

<#if (mviType=='reduce')>
class ${stateClassName}
</#if>

/**
 * StateHolder экрана [${viewClassName}].
 */
@PerScreen
class ${stateHolderClassName} @Inject constructor()<#if (mviType=='reduce')> : State<${stateClassName}>(${stateClassName}())</#if>
