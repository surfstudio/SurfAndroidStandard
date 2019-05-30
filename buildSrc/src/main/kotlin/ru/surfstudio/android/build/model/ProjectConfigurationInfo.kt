package ru.surfstudio.android.build.model

/**
* Представляет информацию о конфигурации проекта
*/
data class ProjectConfigurationInfo(
        val revision: String,
        val components: List<ComponentWithVersion>,
        val libMinSdkVersion: Int,
        val targetSdkVersion: Int,
        val moduleVersionCode: Int,
        val compileSdkVersion: Int
) {
    override fun equals(other: Any?): Boolean {
        val projectInfo = other as ProjectConfigurationInfo
        return this.libMinSdkVersion.equals(projectInfo.libMinSdkVersion) &&
                this.targetSdkVersion.equals(projectInfo.targetSdkVersion) &&
                this.moduleVersionCode.equals(projectInfo.moduleVersionCode) &&
                this.compileSdkVersion.equals(projectInfo.compileSdkVersion)

    }
}