import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.exceptions.ComponentNotFoundException
import ru.surfstudio.android.build.exceptions.DependentComponentsStableException
import ru.surfstudio.android.build.exceptions.PropertyNotDefineException
import ru.surfstudio.android.build.model.module.Library

/**
 * Check components libraries depend on parameter component libraries unstable
 */
open class CheckDependencyForComponentUnstableTask : DefaultTask() {

    @TaskAction
    fun check() {
        val componentName = project.property(GradleProperties.COMPONENT) as? String
                ?: throw PropertyNotDefineException(GradleProperties.COMPONENT)
        val component = Components.value.find { it.name == componentName }
                ?: throw ComponentNotFoundException(componentName)
        val stableLibraries = ArrayList<Library>()

        component.libraries.forEach {
            checkDependentLibrariesStable(stableLibraries, it.name)
        }

        if (stableLibraries.isNotEmpty()) {
            throw DependentComponentsStableException(stableLibraries.map(Library::name))
        }
    }

    private fun checkDependentLibrariesStable(stableLibraries: ArrayList<Library>, libName: String) {
        Components.value.forEach { comp ->
            comp.libraries.forEach { lib ->
                lib.androidStandardDependencies.forEach { dep ->
                    if (libName == dep.name) {
                        if (comp.stable) {
                            stableLibraries.add(lib)
                        }
                        checkDependentLibrariesStable(stableLibraries, lib.name)
                    }
                }
            }
        }
    }
}