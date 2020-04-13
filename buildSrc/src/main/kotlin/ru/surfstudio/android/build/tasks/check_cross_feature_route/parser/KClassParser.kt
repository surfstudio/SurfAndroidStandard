package ru.surfstudio.android.build.tasks.check_cross_feature_route.parser

import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KClassWrapper
import ru.surfstudio.android.build.utils.DefaultGradleLogger
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.utils.GradleLogger
import java.io.File

// todo doc
open class KClassParser : GradleLogger {

    open protected val logger = DefaultGradleLogger("KClassParser", isDebugEnabled = false)

    override var tag: String
        get() = logger.tag
        set(value) {
            logger.tag = value
        }

    override var isDebugEnabled: Boolean
        get() = logger.isDebugEnabled
        set(value) {
            logger.isDebugEnabled = value
        }

    override var isInfoEnabled: Boolean
        get() = logger.isInfoEnabled
        set(value) {
            logger.isInfoEnabled = value
        }

    override var isWarningsEnabled: Boolean
        get() = logger.isWarningsEnabled
        set(value) {
            logger.isWarningsEnabled = value
        }

    override var isErrorsEnabled: Boolean
        get() = logger.isErrorsEnabled
        set(value) {
            logger.isErrorsEnabled = value
        }

    override fun logDebug(message: String) = logger.logDebug(message)

    override fun logInfo(message: String) = logger.logInfo(message)

    override fun logWarning(message: String) = logger.logWarning(message)

    override fun logError(message: String) = logger.logError(message)

    open fun parse(file: File): KClassWrapper? {
        val fileContent = file.readText()
        val packageName = parsePackageName(fileContent)
        val className = parseClassName(fileContent)
        val baseClassName = parseBaseClassName(fileContent)
        val baseClassPackageName = parseBaseClassPackageName(fileContent)
        val implementations = parseImplementations(fileContent)
        val classBody = parseClassBody(fileContent)
        val result = when {
            className.isNotBlank() -> {
                KClassWrapper(
                    packageName,
                    className,
                    baseClassPackageName,
                    baseClassName,
                    implementations,
                    classBody
                )
            }
            else -> null
        }

        when (result) {
            null -> {
                "------------------------------".logd()
                "file: ${file.absolutePath}".logd()
                "UNABLE TO PARSE FILE".logd()
            }
            else -> {
                "------------------------------".logd()
                "file: ${file.absolutePath}".logd()
                "className: $className".logd()
                "baseClassName: $baseClassName".logd()
                "implements: ${implementations.joinToString(", ")}".logd()
                "classPackageName: $packageName".logd()
                "baseClassPackageName: $baseClassPackageName".logd()
            }
        }

        return result
    }

    protected fun findPackageNameRange(target: String): IntRange {
        val packageNameStart = target.indexAfter("package ")
        val packageNameSlice = target.substringSafe(packageNameStart..target.lastIndex)
        val packageNameLen = packageNameSlice.takeWhile { !it.isWhitespace() }.lastIndex
        val packageNameEnd = packageNameStart + packageNameLen
        return packageNameStart..packageNameEnd
    }

    protected fun findClassNameRange(target: String): IntRange {
        val classNameStart = target.indexAfter("class ")
        val classNameSlice = target.substringSafe(classNameStart..target.lastIndex)
        val classNameLen = classNameSlice.takeWhile { it.isLetterOrDigit() }.lastIndex
        val classNameEnd = classNameStart + classNameLen
        return classNameStart..classNameEnd
    }

    protected fun findBaseClassNameRange(target: String): IntRange {
        val implsRange = findImplementationsRange(target)
        val implsSlice = target.substringSafe(implsRange)
        val baseClassNameImplsEnd = implsSlice.indexBefore('(')
        val baseClassNameImplsSlice = implsSlice.substringSafe(0..baseClassNameImplsEnd)
        val baseClassNameLen = baseClassNameImplsSlice.takeLastWhile { it.isLetterOrDigit() }.lastIndex
        val baseClassNameImplsStart = baseClassNameImplsEnd - baseClassNameLen
        val baseClassNameStart = implsRange.first + baseClassNameImplsStart
        val baseClassNameEnd = implsRange.first + baseClassNameImplsEnd
        return baseClassNameStart..baseClassNameEnd
    }

    protected fun findImplementationsRange(target: String): IntRange {
        val implsEnd = target.indexBefore('{')
        val implsSlice = target.substringSafe(0..implsEnd)
        val implsStart = implsSlice.lastIndexAfter(':')
        return implsStart..implsEnd
    }

    protected fun findClassBodyRange(target: String): IntRange {
        val classBodyStart = target.indexAfter('{')
        val classBodyEnd = target.lastIndexOf('}')
        return classBodyStart..classBodyEnd
    }

    protected fun findBaseClassPackageNameRange(target: String, baseClassName: String): IntRange {
        val line = target.lines()
            .find { it.contains("import ") && it.contains(baseClassName) } ?: EMPTY_STRING

        val packageNameLineEnd = line.indexBefore(baseClassName) - 1 // because of dot -> "import com.MyClass"
        val packageNameLineStart = line.indexAfter("import ")
        val importStart = target.indexOf(line)
        val packageNameStart = importStart + packageNameLineStart
        val packageNameEnd = importStart + packageNameLineEnd
        return packageNameStart..packageNameEnd
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

    protected fun String.indexBefore(target: String): Int {
        val index = indexOf(target)
        return if (index >= 0) index - 1 else index
    }

    protected fun String.indexBefore(target: Char): Int {
        val index = indexOf(target)
        return if (index >= 0) index - 1 else index
    }

    private fun parsePackageName(target: String): String {
        return target.substringSafe(findPackageNameRange(target))
    }

    private fun parseClassName(target: String): String {
        return target.substringSafe(findClassNameRange(target))
    }

    private fun parseImplementations(target: String): List<String> {
        return target.substringSafe(findImplementationsRange(target))
            .split(',')
            .map { it.trim() }
    }

    private fun parseBaseClassName(target: String): String {
        return target.substringSafe(findBaseClassNameRange(target))
    }

    private fun parseBaseClassPackageName(target: String): String {
        val baseClassName = parseBaseClassName(target)
        return target.substringSafe(findBaseClassPackageNameRange(target, baseClassName))
    }

    private fun parseClassBody(target: String): String {
        return target.substringSafe(findClassBodyRange(target))
    }
}