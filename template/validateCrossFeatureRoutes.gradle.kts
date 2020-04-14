tasks.register<ValidateCrossFeatureRoutesTask>("validateCrossFeatureRoutesTask") {
    shouldSkipValidation = false
    ignoredFileNames = listOf("build")
}

tasks.whenTaskAdded {
    val isPreBuildTask = name.contains("pre") && name.contains("Build")
    if (isPreBuildTask) dependsOn(tasks.named("validateCrossFeatureRoutesTask"))
}

/**
 * Task performs `CrossFeatureRoute's` validation. Check's following conditions:
 * 1. `CrossFeatureRoute` have valid `targetClassPath`?;
 * 2. Target view (`Fragment` or `Dialog`) of `CrossFeatureRoute` implements `CrossFeatureFragment` interface?;
 * */
open class ValidateCrossFeatureRoutesTask : DefaultTask() {

    /**
     * Result of the directory scanning.
     *
     * @param routes list of routes found in this directory and it's sub-directories.
     * @param views list of views found in this directory and it's sub-directories.
     * */
    private data class DirectoryScanResult(val routes: List<File>, val views: List<File>)

    /**
     * Whenever this value is true -> this task gonna be skipped with console warning.
     * */
    var shouldSkipValidation: Boolean = false
    
    /**
     * List of file/directory names which gonna be ignored on project scan.
     * */
    var ignoredFileNames: List<String> = emptyList()

    /**
     * Validate all of CrossFeatureRoute's in this project.
     * */
    @TaskAction
    fun validate() {
        if (shouldSkipValidation) {
            logger.warn("Validation of CrossFeatureRoute's disabled.")
            return
        }

        logger.info("Validating CrossFeatureRoute's...")
        val directoryScanResult = scanDirectory(project.rootDir)
        val routeParser = KClassCrossFeatureRouteParser(logger)
        val viewParser = KClassCrossFeatureViewParser(logger)

        val crossFeatureRouteWrappers = directoryScanResult.routes
                .mapNotNull { routeParser.parse(it) }
                .filterIsInstance<KClassCrossFeatureRouteWrapper>()

        val crossFeatureViewWrappers = directoryScanResult.views
                .mapNotNull { viewParser.parse(it) }
                .filterIsInstance<KClassCrossFeatureViewWrapper>()

        logger.info("Count of CrossFeatureRoute's to validate: ${crossFeatureRouteWrappers.size}")
        logger.info("Count of found views, used in CrossFeatureRoute validation: ${crossFeatureViewWrappers.size}")

        val routeValidator = KClassCrossFeatureRouteValidator(logger, crossFeatureViewWrappers)
        crossFeatureRouteWrappers.forEach { routeValidator.validate(it) }
        logger.info("All CrossFeatureRoute's validated.")
    }

    /**
     * Scan directory and fetch all of sub-directories and files from it.
     * */
    private fun scanDirectory(dir: File): DirectoryScanResult {
        val filteredFiles = dir.listFiles()
                ?.filter { !ignoredFileNames.contains(it.name) } ?: arrayListOf()

        val kotlinFiles = filteredFiles.filter { it.extension == "kt" }
        val directories = filteredFiles.filter { it.isDirectory }

        val directoryRouteFiles = kotlinFiles.filter { it.name.contains("Route") }
        val directoryViewFiles = kotlinFiles.filter { it.name.contains("View") }

        val scanResults = mutableListOf<DirectoryScanResult>().apply {
            add(DirectoryScanResult(directoryRouteFiles, directoryViewFiles))
        }

        directories.forEach { directory ->
            scanDirectory(directory).also { scanResults.add(it) }
        }

        val allRouteFiles = scanResults.flatMap { it.routes }
        val allViewFiles = scanResults.flatMap { it.views }
        return DirectoryScanResult(allRouteFiles, allViewFiles)
    }
}

//region data

/**
 * Wrapper that contains parsed information of kotlin class file.
 * */
open class KClassWrapper(
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

/**
 * Wrapper that contains parsed information of kotlin class file.
 * */
class KClassCrossFeatureViewWrapper(
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

/**
 * Wrapper that contains parsed information of kotlin class file.
 * */
class KClassCrossFeatureRouteWrapper(
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
        return "${super.toString()} (target = $targetClassPath)"
    }
}

//endregion

//region utils

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
                .find { it.contains("import ") && it.contains(baseClassName) } ?: ""

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
}

/**
 * Kotlin class file parser.
 *
 * Parses all of views without any filtration because CrossFeatureRoute can be implemented
 * in base classуs and we must have possibility to reach those base classes.
 * */
class KClassCrossFeatureViewParser(logger: Logger) : KClassParser(logger) {

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
        logger.info("$entityName parsed: $parsingResult")

        return result
    }
}

/**
 * Kotlin class file parser.
 *
 * Parses only routes that implements CrossFeatureRoute.
 * */
class KClassCrossFeatureRouteParser(logger: Logger) : KClassParser(logger) {

    override fun parse(file: File): KClassWrapper? {
        val parsingResult = super.parse(file) ?: return null

        val names = listOf(parsingResult.className, parsingResult.baseClassName)
        val isActivity: Boolean = names.any { it.contains("Activity") }
        val isFragment: Boolean = names.any { it.contains("Fragment") }
        val isDialog: Boolean = names.any { it.contains("Dialog") }
        val targetClassPath = parseTargetClassPath(parsingResult.classBody)
        val isCrossFeatureRoute = targetClassPath.isNotBlank()

        val result = when (isCrossFeatureRoute) {
            true -> {
                KClassCrossFeatureRouteWrapper(
                        parsingResult.packageName,
                        parsingResult.className,
                        parsingResult.baseClassPackageName,
                        parsingResult.baseClassName,
                        parsingResult.implementations,
                        parsingResult.classBody,
                        targetClassPath
                )
            }
            else -> null
        }

        val entityName = when {
            isActivity -> "ActivityRoute"
            isFragment -> "FragmentRoute"
            isDialog -> "DialogRoute"
            else -> "Route"
        }

        when (result) {
            null -> logger.info("$entityName ignored: $parsingResult")
            else -> logger.info("$entityName parsed: $parsingResult")
        }

        return result
    }

    private fun parseTargetClassPath(classBody: String): String {
        return classBody.substringSafe(findTargetClassPathRange(classBody))
    }

    private fun findTargetClassPathRange(target: String): IntRange {
        val funStart = target.indexAfter("fun targetClassPath()")
        val funSlice = target.substringSafe(funStart..target.lastIndex)
        val start = funStart + funSlice.indexAfter('"')
        val targetClassPathSlice = target.substringSafe(start..target.lastIndex)
        val end = start + targetClassPathSlice.indexBefore('"')
        return start..end
    }

}

/**
 * Validate's CrossFeatureRoute by following steps:
 * 1. Check is target view exist;
 * 2. Check is target view Activity or implements CrossFeatureFragment;
 *
 * @param logger entity to log message in to.
 * @param views list of views, used to validate CrossFeatureRoute.
 * */
class KClassCrossFeatureRouteValidator(
        private val logger: Logger,
        private val views: List<KClassCrossFeatureViewWrapper>
) {

    /**
     * Validate CrossFeatureRoute.
     * */
    fun validate(route: KClassCrossFeatureRouteWrapper) {
        val routeTargetView = findViewClass(route.targetClassPackageName, route.targetClassName)
                ?: error("Unable to find view for $route")

        val isViewImplementsCrossFeature = checkIsImplementsCrossFeature(routeTargetView)
        if (!isViewImplementsCrossFeature) {
            error("$routeTargetView is not implements CrossFeatureFragment for $route")
        }

        logger.info("Verified: $route")
    }

    private fun checkIsImplementsCrossFeature(view: KClassCrossFeatureViewWrapper): Boolean {
        return when {
            view.isImplementsCrossFeature -> true
            else -> {
                val routeTargetParent = findViewClass(view.baseClassName, view.baseClassPackageName)
                routeTargetParent?.let(::checkIsImplementsCrossFeature) ?: false
            }
        }
    }

    private fun findViewClass(packageName: String, className: String): KClassCrossFeatureViewWrapper? {
        return views.find { it.className == className && it.packageName == packageName }
    }
}

//endregion