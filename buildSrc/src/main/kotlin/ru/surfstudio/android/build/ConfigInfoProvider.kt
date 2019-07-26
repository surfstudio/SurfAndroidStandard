package ru.surfstudio.android.build

import com.google.gson.GsonBuilder
import ru.surfstudio.android.build.model.GlobalConfigInfo
import ru.surfstudio.android.build.model.json.ConfigInfoJson
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.utils.JsonHelper
import java.io.File

private const val CONFIG_INFO_JSON_FILE_PATH = "buildSrc/projectConfiguration.json"

/**
 * Class to work with project configInfo
 */
object ConfigInfoProvider {

    /**
     * specified only when running settings for ability to add android standard locally
     * otherwise will be empty
     */
    var currentDirectory = EMPTY_STRING

    private val configInfoJsonFile: File by lazy { openConfigInfoFile() }
    val globalConfigInfo: GlobalConfigInfo by lazy { parseProjectConfigInfoJson() }

    fun incrementUnstableVersion() {
        val configInfo = globalConfigInfo.copy(unstableVersion = globalConfigInfo.unstableVersion + 1)
        JsonHelper.write(ConfigInfoJson(configInfo), configInfoJsonFile)
    }

    fun incrementProjectSnapshotVersion() {
        val configInfo = globalConfigInfo.copy(projectSnapshotVersion = globalConfigInfo.projectSnapshotVersion + 1)
        JsonHelper.write(ConfigInfoJson(configInfo), configInfoJsonFile)
    }

    private fun parseProjectConfigInfoJson(): GlobalConfigInfo {
        return GsonBuilder().create()
                .fromJson(configInfoJsonFile.reader(), ConfigInfoJson::class.java)
                .transform()
    }

    private fun openConfigInfoFile() = File(currentDirectory + CONFIG_INFO_JSON_FILE_PATH)

    @JvmStatic
    private fun getVersion() = globalConfigInfo.version
}