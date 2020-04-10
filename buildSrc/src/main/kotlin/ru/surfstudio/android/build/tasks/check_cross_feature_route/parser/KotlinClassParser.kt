package ru.surfstudio.android.build.tasks.check_cross_feature_route.parser

import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KotlinClassWrapper
import ru.surfstudio.android.build.utils.DefaultGradleLogger
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.utils.GradleLogger
import java.io.File

open class KotlinClassParser(isDebugEnabled: Boolean = false) :
        GradleLogger by DefaultGradleLogger("KotlinClassFileParser", isDebugEnabled) {

    private companion object {
        const val FORMAT_PACKAGE_NAME_START = "package "
        const val FORMAT_CLASS_NAME_START = "class "
        const val FORMAT_IMPORT = "import "

        const val FORMAT_BASE_CLASS_NAME_END = ')'

        const val FORMAT_IMPLEMENTATIONS_START = ':'
        const val FORMAT_IMPLEMENTATIONS_DELIMITER = ':'
        const val FORMAT_IMPLEMENTATIONS_END = '{'

        const val FORMAT_CLASS_BODY_START = " {"
        const val FORMAT_CLASS_BODY_END = '}'
    }

    open fun parse(file: File): KotlinClassWrapper? {
        val fileContent = file.readText()
        val packageName = parsePackageName(fileContent)
        val className = parseClassName(fileContent)
        val baseClassName = parseBaseClassName(fileContent)
        val baseClassPackageName = parseBaseClassPackageName(fileContent)
        val implementations = parseImplementations(fileContent)
        val classBody = parseClassBody(fileContent)

        "------------------------------".logd()
        "file: ${file.absolutePath}".logd()
        "packageName: $packageName".logd()
        "className: $className".logd()
        "baseClassName: $baseClassName".logd()
        "implements: ${implementations.joinToString(", ")}".logd()

        return if (className.isBlank()) null
        else KotlinClassWrapper(
                packageName,
                className,
                baseClassName,
                baseClassPackageName,
                implementations,
                classBody
        )
    }

    private fun parsePackageName(target: String): String {
        return target.substringSafe(findPackageNameRange(target))
    }

    private fun parseClassName(target: String): String {
        return target.substringSafe(findClassNameRange(target))
    }

    private fun parseImplementations(target: String): List<String> {
        return target.substringSafe(findImplementationsRange(target))
                .trim()
                .split(FORMAT_IMPLEMENTATIONS_DELIMITER)
    }

    private fun parseBaseClassName(target: String): String {
        return parseImplementations(target)
                .filter { item -> item.lastOrNull() == FORMAT_BASE_CLASS_NAME_END }
                .map { item -> item.takeWhile { it.isLetterOrDigit() } }
                .firstOrNull() ?: EMPTY_STRING
    }

    private fun parseBaseClassPackageName(target: String): String {
        val baseClassName = parseBaseClassName(target)
        return target.substringSafe(findBaseClassPackageNameRange(target, baseClassName))
    }

    private fun parseClassBody(target: String): String {
        return target.substringSafe(findClassBodyRange(target))
    }

    private fun findPackageNameRange(target: String): IntRange {
        val packageNameStart = target.indexAfter(FORMAT_PACKAGE_NAME_START)
        val packageNameSlice = target.substringSafe(packageNameStart..target.lastIndex)
        val packageNameEnd = packageNameStart + packageNameSlice.indexOfFirst { it.isWhitespace() } - 1
        return packageNameStart..packageNameEnd
    }

    private fun findClassNameRange(target: String): IntRange {
        val classNameStart = target.indexAfter(FORMAT_CLASS_NAME_START)
        val classNameSlice = target.substringSafe(classNameStart..target.lastIndex)
        val classNameEnd = classNameStart + classNameSlice.indexOfFirst { !it.isLetterOrDigit() } - 1
        return classNameStart..classNameEnd
    }

    private fun findImplementationsRange(target: String): IntRange {
        val classStart = target.indexOf(FORMAT_CLASS_NAME_START)
        val classSlice = target.substringSafe(classStart..target.lastIndex)
        val implementationsEnd = classStart + classSlice.indexBefore(FORMAT_IMPLEMENTATIONS_END)
        val implementationsSlice = classSlice.substringSafe(0..implementationsEnd)
        val implementationsStart = classStart + implementationsSlice.lastIndexAfter(FORMAT_IMPLEMENTATIONS_START)
        return implementationsStart..implementationsEnd
    }

    private fun findClassBodyRange(target: String): IntRange {
        val classBodyStart = target.indexAfter(FORMAT_CLASS_BODY_START)
        val classBodyEnd = target.lastIndexOf(FORMAT_CLASS_BODY_END)
        return classBodyStart..classBodyEnd
    }

    private fun findBaseClassPackageNameRange(target: String, name: String): IntRange {
        val line = target.lines()
                .find { it.contains(FORMAT_IMPORT) && it.contains(name) } ?: EMPTY_STRING

        val start = line.indexOf(name)
        val end = line.lastIndex
        return start..end
    }

    protected fun String.substringSafe(range: IntRange): String {
        return try {
            substring(range)
        } catch (error: Throwable) {
            EMPTY_STRING
        }
    }

    protected fun String.indexAfter(target: String): Int {
        val index = indexOf(target)
        return if (index >= 0) index + target.length else index
    }

    protected fun String.indexAfter(target: Char): Int {
        val index = indexOf(target)
        return if (index >= 0) index + 1 else index
    }

    protected fun String.lastIndexAfter(target: String): Int {
        val index = lastIndexOf(target)
        return if (index >= 0) index + target.length else index
    }

    protected fun String.lastIndexAfter(target: Char): Int {
        val index = lastIndexOf(target)
        return if (index >= 0) index + 1 else index
    }

    protected fun String.indexBefore(target: Char): Int {
        val index = indexOf(target)
        return if (index >= 0) index - 1 else index
    }

}