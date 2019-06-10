package ru.surfstudio.android.build

import com.google.gson.GsonBuilder
import ru.surfstudio.android.build.model.ConfigInfo
import ru.surfstudio.android.build.model.json.ConfigInfoJson
import ru.surfstudio.android.build.tasks.changed_components.JsonHelper
import java.io.File

private const val CONFIG_INFO_JSON_FILE_PATH = "configInfo.json"

/**
 * Class to work with project configInfo
 */
object ConfigInfoProvider {

    private val configInfoJsonFile = File(CONFIG_INFO_JSON_FILE_PATH)

    var configInfo: ConfigInfo = parseProjectConfigInfoJson()

    fun incrementUnstableVersion() {
        configInfo = configInfo.copy(unstableVersion = configInfo.unstableVersion + 1)
        JsonHelper.write(ConfigInfoJson(configInfo), configInfoJsonFile)
    }

    fun incrementProjectSnapshotVersion() {
        configInfo = configInfo.copy(projectSnapshotVersion = configInfo.projectSnapshotVersion + 1)
        JsonHelper.write(ConfigInfoJson(configInfo), configInfoJsonFile)
    }

    private fun parseProjectConfigInfoJson(): ConfigInfo = GsonBuilder().create()
            .fromJson(configInfoJsonFile.reader(), ConfigInfoJson::class.java)
            .transform()

    @JvmStatic
    private fun getVersion(): String = configInfo.version
}