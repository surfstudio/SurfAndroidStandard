package ru.surfstudio.android.build

import ru.surfstudio.android.build.exceptions.CantReadFileException
import ru.surfstudio.android.build.exceptions.NoPropertyDefinedInFileException
import ru.surfstudio.android.build.utils.EMPTY_STRING
import java.io.File
import java.io.FileInputStream
import java.util.*

const val GRADLE_PROPERTIES_FILE_PATH = "mirror.properties"
const val MIRROR_COMPONENT_NAME = "surf.mirrorComponentName"

/**
 * Loading properties and caching them from [GRADLE_PROPERTIES_FILE_PATH]
 * Created for loading only once during settings.gradle execute and then use everywhere
 */
object GradlePropertiesManager {
    private var componentMirrorName: String = EMPTY_STRING

    init {
        loadMirrorComponentName()
    }

    /**
     * get current mirror component name
     *
     * @return component mirror name
     */
    fun getMirrorComponentName(): String {
        return componentMirrorName
    }

    /**
     * check if current component is mirror
     *
     * @return true if mirror
     */
    fun isCurrentComponentAMirror() = componentMirrorName != EMPTY_STRING

    /**
     * gets component mirror name as property from file [GRADLE_PROPERTIES_FILE_PATH]
     * if there is no such file then current repository is not mirror
     */
    private fun loadMirrorComponentName() {
        val props = Properties()
        val propFile = File(GRADLE_PROPERTIES_FILE_PATH)
        if (!propFile.exists()) return
        if (propFile.canRead()) {
            props.load(FileInputStream(propFile))
            if (props.containsKey(MIRROR_COMPONENT_NAME)) {
                componentMirrorName = props[MIRROR_COMPONENT_NAME] as String
            } else {
                throw NoPropertyDefinedInFileException(MIRROR_COMPONENT_NAME, GRADLE_PROPERTIES_FILE_PATH)
            }
        } else {
            throw CantReadFileException(GRADLE_PROPERTIES_FILE_PATH)
        }
    }
}