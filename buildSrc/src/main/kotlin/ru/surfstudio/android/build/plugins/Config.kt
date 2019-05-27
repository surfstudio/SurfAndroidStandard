package ru.surfstudio.android.build.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.surfstudio.android.build.components
import ru.surfstudio.android.build.model.Dependency

class Config : Plugin<Project> {

    private lateinit var project: Project

    override fun apply(project: Project) {
        this.project = project

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

    private fun configurateThirdPartyDependancies(deps: List<Dependency>) {
        deps.forEach {
            val depName = "${it.name}:${project.property(it.name)}"
            project.dependencies.add(it.type, depName)
        }
    }

    private fun configurateAndroidStandardDependencies(deps: List<Dependency>) {
        deps.forEach {
            val depName = project.rootProject.project(":${it.name}")
            project.dependencies.add(it.type, depName)
            /*
            Возможно нужно еще добавить эим зависимости

            compileOnly context.project(":$it")
            testImplementation context.project(":$it")
            androidTestImplementation context.project(":$it")*/
        }
    }
}