package ru.surfstudio.android.build.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.surfstudio.android.build.components
import ru.surfstudio.android.build.LIBRARY_VERSIONS_KEY
import ru.surfstudio.android.build.addDependency
import ru.surfstudio.android.build.model.json.DependencyJson

class Config : Plugin<Project> {

    private lateinit var project: Project
    private var libraryVersions: Map<String, String> = emptyMap()

    override fun apply(project: Project) {
        this.project = project
        libraryVersions = project.property(LIBRARY_VERSIONS_KEY) as Map<String, String>

        configurateDependancies()
    }

    /**
     * Configurate dependancies
     */
    private fun configurateDependancies() {
        components.forEach { component ->
            component.libs
                    .find { it.name == project.name }
                    ?.let {
                        configurateThirdPartyDependancies(it.thirdPartyDependencies)
                        configurateAndroidStandardDependencies(it.androidStandardDependencies)
                    }
        }
    }

    private fun configurateThirdPartyDependancies(deps: List<DependencyJson>) {
        deps.forEach { addDependency(project, it.name, it.type) }
    }

    private fun configurateAndroidStandardDependencies(deps: List<DependencyJson>) {
        deps.forEach {
            val depend = project.rootProject.project(":${it.name}")
            project.dependencies.add(it.type, depend)
            /*
            Возможно нужно еще добавить эим зависимости

            compileOnly context.project(":$it")
            testImplementation context.project(":$it")
            androidTestImplementation context.project(":$it")*/
        }
    }
}