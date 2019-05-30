package ru.surfstudio.android.build.tasks.check_components

import ru.surfstudio.android.build.model.CheckComponentsConfigurationResult
import ru.surfstudio.android.build.model.ComponentWithVersion
import ru.surfstudio.android.build.model.ProjectConfigurationInfo

/**
 * Helper class for comparing two ProjectInfo objects, representing info about project configuration state
 */
class ProjectInfosComparator {

    private val errorNoComponent = "No information about component in second revision: "
    private val component = "Component:"
    private val errorLibsListDiffer = "Libraries lists are different (count or version)"
    private val errorGeneralValuesDiffer = "One of the following values differ: libMinSdkVersion,targetSdkVersion," +
            "compileSdkVersion, moduleVersionCode"

    fun compareProjectInfos(
            firstProjectConfigurationInfo: ProjectConfigurationInfo,
            secondProjectConfigurationInfo: ProjectConfigurationInfo
    ): CheckComponentsConfigurationResult {
        val resultGenerals = compareGeneralValues(firstProjectConfigurationInfo, secondProjectConfigurationInfo)
        return if (!resultGenerals.isOk) resultGenerals
        else compareAllComponents(firstProjectConfigurationInfo.components, secondProjectConfigurationInfo.components)
    }

    private fun compareGeneralValues(
            firstProjectConfigurationInfo: ProjectConfigurationInfo,
            secondProjectConfigurationInfo: ProjectConfigurationInfo
    ): CheckComponentsConfigurationResult {
        return if (!firstProjectConfigurationInfo.equals(secondProjectConfigurationInfo))
            CheckComponentsConfigurationResult(false, errorGeneralValuesDiffer)
        else CheckComponentsConfigurationResult(true)
    }

    private fun compareAllComponents(
            first: List<ComponentWithVersion>,
            second: List<ComponentWithVersion>
    ): CheckComponentsConfigurationResult {
        val pairs = first.filter { it.isStable }.map { componentWithVersion ->
            componentWithVersion to second.find { it.id == componentWithVersion.id }
        }
        for (pair in pairs) {
            if (pair.second == null) return CheckComponentsConfigurationResult(false, "$errorNoComponent ${pair.first.id}")
            val result = compareComponents(pair.first, pair.second!!)
            if (!result.isOk) return result
        }

        return CheckComponentsConfigurationResult(true)
    }

    private fun compareComponents(first: ComponentWithVersion, second: ComponentWithVersion): CheckComponentsConfigurationResult {
        return if (!listsAreEqual(first.libs, second.libs))
            CheckComponentsConfigurationResult(false, "$component ${first.id}. $errorLibsListDiffer")
        else
            CheckComponentsConfigurationResult(true)
    }

    private fun <T> listsAreEqual(listFirst: List<T>, listSecond: List<T>): Boolean {
        return hashSetOf(listFirst).equals(hashSetOf(listSecond))
    }
}