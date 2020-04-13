package ru.surfstudio.android.build.tasks.check_cross_feature_route.parser

import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KClassCrossFeatureViewWrapper
import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KClassWrapper
import java.io.File

/**
 * Парсер View-классов.
 *
 * Парсит все вью без исключения, т.к. `CrossFeatureRoute-интерфейс` может быть
 * имплементирован в базовых классах, к которым мы должны иметь доступ.
 * */
class KClassCrossFeatureViewParser : KClassParser() {

    override var tag = "KClassCrossFeatureViewParser"

    override fun parse(file: File): KClassWrapper? {
        val parsingResult = super.parse(file) ?: return null

        val names = listOf(parsingResult.className, parsingResult.baseClassName)
        val isActivity: Boolean = names.any { it.contains("Activity") }
        val isFragment: Boolean = names.any { it.contains("Fragment") }
        val isDialog: Boolean = names.any { it.contains("Dialog") }
        val isImplementsCrossFeature: Boolean = when {
            isActivity -> true
            isFragment || isDialog -> parsingResult.implementations.contains("CrossFeatureFragment")
            else -> false
        }

        val result = KClassCrossFeatureViewWrapper(
                parsingResult.packageName,
                parsingResult.className,
                parsingResult.baseClassPackageName,
                parsingResult.baseClassName,
                parsingResult.implementations,
                parsingResult.classBody,
                isImplementsCrossFeature
        )

        val entityName = when {
            isActivity -> "ActivityView"
            isFragment -> "FragmentView"
            isDialog -> "DialogView"
            else -> "View"
        }
        "$entityName parsed: $parsingResult".logi()

        return result
    }
}