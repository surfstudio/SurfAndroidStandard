package ru.surfstudio.android.build

import ru.surfstudio.android.build.exceptions.CantReadFileException
import ru.surfstudio.android.build.exceptions.property.NoPropertyDefinedInFileException
import ru.surfstudio.android.build.utils.EMPTY_STRING
import java.io.File
import java.io.FileInputStream
import java.util.*

/**
 * These files will be searched in local project's directory,
 * if the local android standard connection is active,
 * otherwise these files will be searched in android standard directory as usual
 */
const val MIRROR_PROPERTIES_FILE_PATH = "mirror.properties"
private const val ANDROID_STANDARD_PROPERTIES_FILE_PATH = "android-standard/androidStandard.properties"

const val COMMON_COMPONENT_NAME = "surf.commonComponentName"
private const val MIRROR_COMPONENT_NAME = "surf.mirrorComponentName"
private const val SKIP_SAMPLES_BUILD_PROPERTY_NAME = "skipSamplesBuild"

/**
 * Loading properties and caching them
 * from [MIRROR_PROPERTIES_FILE_PATH] and [ANDROID_STANDARD_PROPERTIES_FILE_PATH]
 * Created for loading only once during settings.gradle execute and then use everywhere
 */
object GradlePropertiesManager {

    var componentMirrorName: String = EMPTY_STRING
        private set

    var commonComponentNameForMirror: String = EMPTY_STRING
        private set

    var skipSamplesBuilding: Boolean = false
        private set

    fun init() {
        loadMirrorComponentName()
        loadCommonComponentNameForMirror()
        loadSkipSamplesBuildProperty()
    }

    /**
     * check if the current component is mirror
     *
     * @return true if mirror
     */
    @JvmStatic
    fun isCurrentComponentAMirror(): Boolean = componentMirrorName.isNotEmpty()

    /**
     * check if the current component has common component as module.
     * The function is used only for mirror components.
     *
     * @return true if the current component has common component
     */
    fun hasCommonComponent(): Boolean = commonComponentNameForMirror.isNotEmpty()

    /**
     * load property from file
     *
     * @return property value
     */
    fun loadProperty(
            propertiesFileName: String,
            propertyName: String,
            required: Boolean = true
    ): String? {
        val props = Properties()
        val propFile = File(propertiesFileName)
        if (!propFile.exists()) return null
        return if (propFile.canRead()) {
            props.load(FileInputStream(propFile))
            if (props.containsKey(propertyName)) {
                props[propertyName].toString()
            } else {
                if (required) {
                    throw NoPropertyDefinedInFileException(propertyName, propertiesFileName)
                }
                println("WARNING: $propertyName not found in $propertiesFileName")
                null
            }
        } else {
            throw CantReadFileException(propertiesFileName)
        }
    }

    /**
     * gets component mirror name as property from file [MIRROR_PROPERTIES_FILE_PATH]
     * if there is no such file then current repository is not mirror
     */
    private fun loadMirrorComponentName() {
        loadProperty(
                propertiesFileName = MIRROR_PROPERTIES_FILE_PATH,
                propertyName = MIRROR_COMPONENT_NAME
        )?.also { propertyValue ->
            componentMirrorName = propertyValue
        }
    }

    private fun loadCommonComponentNameForMirror() {
        loadProperty(
                propertiesFileName = MIRROR_PROPERTIES_FILE_PATH,
                propertyName = COMMON_COMPONENT_NAME,
                required = false
        )?.also { propertyValue ->
            commonComponentNameForMirror = propertyValue
        }
    }

    private fun loadSkipSamplesBuildProperty() {
        loadProperty(
                propertiesFileName = ANDROID_STANDARD_PROPERTIES_FILE_PATH,
                propertyName = SKIP_SAMPLES_BUILD_PROPERTY_NAME
        )?.also { propertyValue ->
            skipSamplesBuilding = propertyValue.toBoolean()
        }
    }
}