package ${packageName}

import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
<#if shouldAddNavigationMiddleware>
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.NavigationMiddleware
</#if>
import io.reactivex.Observable
import ${packageName}.${eventClassName}.*

@PerScreen
internal class ${middlewareClassName} @Inject constructor(
    baseMiddlewareDependency: BaseMiddlewareDependency<#if shouldAddNavigationMiddleware>,
    private val navigationMiddleware: NavigationMiddleware</#if>
) : BaseMiddleware<${eventClassName}>(baseMiddlewareDependency) {

    override fun transform(eventStream: Observable<${eventClassName}>) = 
        transformations(eventStream) { 
            <#if shouldAddNavigationMiddleware>
            addAll(
                Navigation::class decomposeTo navigationMiddleware
            )
            <#else>
            /* do nothing */
            </#if>
        }
}

