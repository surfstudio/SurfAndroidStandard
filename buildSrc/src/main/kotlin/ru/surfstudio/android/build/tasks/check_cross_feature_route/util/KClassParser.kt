package ru.surfstudio.android.build.tasks.check_cross_feature_route.util

import org.gradle.api.logging.Logger
import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KClassWrapper
import ru.surfstudio.android.build.utils.EMPTY_STRING
import java.io.File

/**
 * Kotlin class file parser.
 *
 * @param logger entity to log messages in to.
 * */
open class KClassParser(protected val logger: Logger) {

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
                logger.debug("------------------------------")
                logger.debug("UNABLE TO PARSE FILE: ${file.absolutePath}")
            }
            else -> {
                logger.debug("------------------------------")
                logger.debug("file: ${file.absolutePath}")
                logger.debug("className: $className")
                logger.debug("baseClassName: $baseClassName")
                logger.debug("implements: ${implementations.joinToString(", ")}")
                logger.debug("classPackageName: $packageName")
                logger.debug("baseClassPackageName: $baseClassPackageName")
            }
        }

        return result
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
}