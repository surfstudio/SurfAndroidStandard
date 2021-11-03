tasks.register<ValidateCrossFeatureRoutesTask>("validateCrossFeatureRoutesTask") {
    shouldSkipValidation = false
    ignoredDirectories = listOf("build")
    routeFilterCondition = { file ->
        file.name.contains("Route")
    }
    viewFilterCondition = { file ->
        file.name.contains("Activity") ||
                file.name.contains("Fragment") ||
                file.name.contains("Dialog")
    }

    // TODO: изменить на true, если используется последняя навигация
    useTheMostRecentNavigation = false
}

tasks.whenTaskAdded {
    val isPreBuildTask = name.contains("pre") && name.contains("Build")
    if (isPreBuildTask) dependsOn(tasks.named("validateCrossFeatureRoutesTask"))
}

/**
 * Task performs `CrossFeatureRoute's` validation. Check's following conditions:
 * 1. Route have valid `targetClassPath` or `getScreenClassPath` depending on [useTheMostRecentNavigation];
 * 2. Target view (`Fragment` or `Dialog`) of `CrossFeatureRoute` implements `CrossFeatureFragment` interface?;
 *
 * **Note**: supported only `.kt` source files parsing.
 *
 * ## You can setup this task by modifying these fields:
 * * [shouldSkipValidation];
 * * [ignoredDirectories];
 * * [routeFilterCondition];
 * * [viewFilterCondition];
 * * [useTheMostRecentNavigation].
 * */
open class ValidateCrossFeatureRoutesTask : DefaultTask() {

    /**
     * Whenever this value is true -> this task gonna be skipped with console warning.
     * */
    @Internal
    var shouldSkipValidation: Boolean = false

    /**
     * List of directory names which gonna be ignored on project scan.
     * */
    @Internal
    var ignoredDirectories: List<String> = emptyList()

    /**
     * Condition used to find routes.
     * */
    @Internal
    var routeFilterCondition: (File) -> Boolean = { false }

    /**
     * Condition used to find views that can be Route target.
     * */
    @Internal
    var viewFilterCondition: (File) -> Boolean = { false }

    /**
     *  Validate new navigation routes.
     * */
    @Internal
    var useTheMostRecentNavigation: Boolean = false

    @TaskAction
    fun validate() {
        if (shouldSkipValidation) {
            logger.warn("Validation of CrossFeatureRoute's disabled.")
            return
        }

        logger.info("Validating CrossFeatureRoute's...")
        val foundFiles = ProjectFileTreeScanner().scan(project.rootDir, ignoredDirectories) {
            it.extension == "kt" && (routeFilterCondition(it) || viewFilterCondition(it))
        }

        val foundRoutes = foundFiles.filter { routeFilterCondition(it) }
        val foundViews = foundFiles.filter { viewFilterCondition(it) }

        val routeParser = getRouteParser()
        val viewParser = CrossFeatureViewParser(logger)

        val crossFeatureRouteFiles = foundRoutes
                .mapNotNull { routeParser.parse(it) }
                .filterIsInstance<CrossFeatureRouteFile>()

        val crossFeatureViewFiles = foundViews
                .mapNotNull { viewParser.parse(it) }
                .filterIsInstance<CrossFeatureViewFile>()

        if(crossFeatureRouteFiles.isEmpty()) {
            logger.warn("No cross feature routes found. Check isNewNavigationUsed flag.")
        }

        logger.info("Count of CrossFeatureRoute's to validate: ${crossFeatureRouteFiles.size}")
        logger.info("Count of found views, used in CrossFeatureRoute validation: ${crossFeatureViewFiles.size}")

        val routeValidator = CrossFeatureRouteValidator(crossFeatureViewFiles, logger)
        crossFeatureRouteFiles.forEach { routeValidator.validate(it) }
        logger.info("All CrossFeatureRoute's validated.")
    }

    private fun getRouteParser(): BaseRouteParser {
        return when {
            useTheMostRecentNavigation -> ScreenPathBasedRouteParser(logger)
            else -> TargetPathBasedRouteParser(logger)
        }
    }
}

//region utils

private class ProjectFileTreeScanner {

    fun scan(
            directory: File,
            ignoredDirectories: List<String> = emptyList(),
            filterCondition: (File) -> Boolean
    ): List<File> {
        val content = directory.listFiles() ?: emptyArray()
        val files = content.filter { !it.isDirectory }
        val directories = content.filter { it.isDirectory && !ignoredDirectories.contains(it.name) }
        val filteredFiles = files.filter { filterCondition(it) }

        val scanResult = mutableListOf<File>().apply {
            addAll(filteredFiles)
        }

        directories.forEach { nestedDirectory ->
            scan(nestedDirectory, ignoredDirectories, filterCondition).also {
                scanResult.addAll(it)
            }
        }

        return scanResult
    }
}

private open class KClassParser(protected val logger: Logger? = null) {

    open fun parse(file: File): KClassWrapper? {
        logger?.info("------------------------------")
        logger?.info("Parsing file: ${file.absolutePath}")
        val fileContent = file.readText()
        val packageName = parsePackageName(fileContent)
        val className = parseClassName(fileContent)
        val baseClassName = parseBaseClassName(fileContent)
        val baseClassPackageName = parseBaseClassPackageName(fileContent).let {
            // if we can't find packageName of baseClass - fallback to current packageName
            if (it.isBlank()) packageName else it
        }
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
                logger?.info("Parsing failed.")
            }
            else -> {
                logger?.info("Parsing succeed. Additional info:")
                logger?.info("className: $className")
                logger?.info("baseClassName: $baseClassName")
                logger?.info("implements: ${implementations.joinToString(", ")}")
                logger?.info("classPackageName: $packageName")
                logger?.info("baseClassPackageName: $baseClassPackageName")
            }
        }

        return result
    }

    protected fun String.substringSafe(range: IntRange): String {
        return try {
            substring(range)
        } catch (error: Throwable) {
            ""
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

    private fun findPackageNameRange(target: String): IntRange {
        val packageNameStart = target.indexAfter("package ")
        val packageNameSlice = target.substringSafe(packageNameStart..target.lastIndex)
        val packageNameLen = packageNameSlice.takeWhile { !it.isWhitespace() }.lastIndex
        val packageNameEnd = packageNameStart + packageNameLen
        return packageNameStart..packageNameEnd
    }

    private fun findClassNameRange(target: String): IntRange {
        val classNameStart = target.indexAfter("class ")
        val classNameSlice = target.substringSafe(classNameStart..target.lastIndex)
        val classNameLen = classNameSlice.takeWhile { it.isLetterOrDigit() }.lastIndex
        val classNameEnd = classNameStart + classNameLen
        return classNameStart..classNameEnd
    }

    private fun findBaseClassNameRange(target: String): IntRange {
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

    private fun findImplementationsRange(target: String): IntRange {
        val implsEnd = target.indexBefore('{').let { index ->
            if (index == -1) target.lastIndex else index
        }
        val implsSlice = target.substringSafe(0..implsEnd)
        val implsStart = implsSlice.lastIndexAfter(':')
        return implsStart..implsEnd
    }

    private fun findClassBodyRange(target: String): IntRange {
        val classBodyStart = target.indexAfter('{')
        val classBodyEnd = target.lastIndexOf('}')
        return classBodyStart..classBodyEnd
    }

    private fun findBaseClassPackageNameRange(target: String, baseClassName: String): IntRange {
        val line = target.lines()
                .find { it.contains("import ") && it.contains(baseClassName) } ?: ""

        val packageNameLineEnd = line.indexBefore(baseClassName) - 1 // because of dot -> "import com.MyClass"
        val packageNameLineStart = line.indexAfter("import ")
        val importStart = target.indexOf(line)
        val packageNameStart = importStart + packageNameLineStart
        val packageNameEnd = importStart + packageNameLineEnd
        return packageNameStart..packageNameEnd
    }
}

private class CrossFeatureViewParser(logger: Logger?) : KClassParser(logger) {

    override fun parse(file: File): KClassWrapper? {
        val parsedFile = super.parse(file) ?: return null

        val names = listOf(parsedFile.className, parsedFile.baseClassName)
        val isActivity: Boolean = names.any { it.contains("Activity") }
        val isFragment: Boolean = names.any { it.contains("Fragment") }
        val isDialog: Boolean = names.any { it.contains("Dialog") }
        val isImplementsCrossFeature: Boolean = when {
            isActivity -> true
            isFragment || isDialog -> parsedFile.implementations.contains("CrossFeatureFragment")
            else -> false
        }

        val result = CrossFeatureViewFile(
                parsedFile.packageName,
                parsedFile.className,
                parsedFile.baseClassPackageName,
                parsedFile.baseClassName,
                parsedFile.implementations,
                parsedFile.classBody,
                isImplementsCrossFeature
        )

        val entityName = when {
            isActivity -> "ActivityView"
            isFragment -> "FragmentView"
            isDialog -> "DialogView"
            else -> "View"
        }
        logger?.info("$entityName parsed: $parsedFile")

        return result
    }
}

private class TargetPathBasedRouteParser(logger: Logger?) :
        BaseRouteParser(logger, "fun targetClassPath()")

private class ScreenPathBasedRouteParser(logger: Logger?) :
        BaseRouteParser(logger, "fun getScreenClassPath()")

private abstract class BaseRouteParser(
        logger: Logger?,
        private val methodSignature: String
) : KClassParser(logger) {

    override fun parse(file: File): KClassWrapper? {
        val parsedFile = super.parse(file) ?: return null

        val names = listOf(parsedFile.className, parsedFile.baseClassName)
        val isActivity: Boolean = names.any { it.contains("Activity") }
        val isFragment: Boolean = names.any { it.contains("Fragment") }
        val isDialog: Boolean = names.any { it.contains("Dialog") }
        val targetClassPath = parseTargetClassPath(parsedFile.classBody)
        val isCrossFeatureRoute = targetClassPath.isNotBlank()

        val result = when {
            isCrossFeatureRoute -> CrossFeatureRouteFile(
                    parsedFile.packageName,
                    parsedFile.className,
                    parsedFile.baseClassPackageName,
                    parsedFile.baseClassName,
                    parsedFile.implementations,
                    parsedFile.classBody,
                    targetClassPath
            )

            else -> null
        }

        val entityName = when {
            isActivity -> "ActivityRoute"
            isFragment -> "FragmentRoute"
            isDialog -> "DialogRoute"
            else -> "Route"
        }

        when (result) {
            null -> logger?.info("$entityName ignored: $parsedFile")
            else -> logger?.info("$entityName parsed: $parsedFile")
        }

        return result
    }

    private fun parseTargetClassPath(classBody: String): String {
        return classBody.substringSafe(findTargetClassPathRange(classBody))
    }

    private fun findTargetClassPathRange(target: String): IntRange {
        val funStart = target.indexAfter(methodSignature)
        val funSlice = target.substringSafe(funStart..target.lastIndex)
        val start = funStart + funSlice.indexAfter('"')
        val targetClassPathSlice = target.substringSafe(start..target.lastIndex)
        val end = start + targetClassPathSlice.indexBefore('"')
        return start..end
    }

}

private class CrossFeatureRouteValidator(
        private val views: List<CrossFeatureViewFile>,
        private val logger: Logger? = null
) {

    fun validate(route: CrossFeatureRouteFile) {
        val routeTargetView = findViewClass(route.targetClassPackageName, route.targetClassName)
                ?: error("Unable to find view for $route")

        val isViewImplementsCrossFeature = checkIsImplementsCrossFeature(routeTargetView)
        if (!isViewImplementsCrossFeature) {
            error("$routeTargetView is not implements CrossFeatureFragment for $route")
        }

        logger?.info("Validated: $route")
    }

    private fun checkIsImplementsCrossFeature(view: CrossFeatureViewFile): Boolean {
        return when {
            view.isImplementsCrossFeature -> true
            else -> {
                val baseClass = findViewClass(view.baseClassPackageName, view.baseClassName)
                baseClass?.let(::checkIsImplementsCrossFeature) ?: false
            }
        }
    }

    private fun findViewClass(packageName: String, className: String): CrossFeatureViewFile? {
        return views.find { it.packageName == packageName && it.className == className }
    }
}

//endregion

//region data

private open class KClassWrapper(
        val packageName: String,
        val className: String,
        val baseClassPackageName: String,
        val baseClassName: String,
        val implementations: List<String>,
        val classBody: String
) {
    val fullName: String = "$packageName.$className"

    override fun toString(): String {
        return "$className (package $packageName)"
    }
}

private class CrossFeatureViewFile(
        packageName: String,
        className: String,
        baseClassPackageName: String,
        baseClassName: String,
        implementations: List<String>,
        classBody: String,
        val isImplementsCrossFeature: Boolean
) : KClassWrapper(
        packageName,
        className,
        baseClassPackageName,
        baseClassName,
        implementations,
        classBody
)

private class CrossFeatureRouteFile(
        packageName: String,
        className: String,
        baseClassPackageName: String,
        baseClassName: String,
        implementations: List<String>,
        classBody: String,
        val targetClassPath: String
) : KClassWrapper(
        packageName,
        className,
        baseClassPackageName,
        baseClassName,
        implementations,
        classBody
) {
    val targetClassName: String = targetClassPath.substringAfterLast('.')
    val targetClassPackageName: String = targetClassPath.substringBeforeLast('.')

    override fun toString(): String {
        return when {
            targetClassPath.isBlank() -> super.toString()
            else -> "${super.toString()} (target = $targetClassPath)"
        }
    }
}

//endregion