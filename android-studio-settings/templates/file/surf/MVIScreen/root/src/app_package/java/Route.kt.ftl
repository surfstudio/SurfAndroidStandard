package ${packageName}

import ru.surfstudio.android.core.ui.navigation.feature.route.feature.${routeParentClassName}

/**
 * Роут экрана ${viewClassName}.
 */
class ${routeClassName} : ${routeParentClassName}() {

    override fun targetClassPath() = "${packageName}.${viewClassName}"
}
