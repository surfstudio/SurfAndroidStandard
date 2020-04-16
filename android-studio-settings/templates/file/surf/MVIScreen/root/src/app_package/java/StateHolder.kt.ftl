package ${packageName}

import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject
<#if (mviType=='reduce')>
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
</#if>

<#if (mviType=='reduce')>
//TODO обязательно добавить ключевое слово "data" и хотя бы одно поле. Это необходимо для корректного вычисления диффа между стейтами.
internal class ${stateClassName}
</#if>

@PerScreen
internal class ${stateHolderClassName} @Inject constructor()<#if (mviType=='reduce')> : State<${stateClassName}>(${stateClassName}())</#if>
