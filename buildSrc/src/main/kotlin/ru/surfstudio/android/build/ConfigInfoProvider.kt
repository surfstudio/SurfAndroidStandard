package ru.surfstudio.android.build

import com.google.gson.GsonBuilder
import ru.surfstudio.android.build.model.GlobalConfigInfo
import ru.surfstudio.android.build.model.json.ConfigInfoJson
import ru.surfstudio.android.build.tasks.changed_components.JsonHelper
import java.io.File

private const val CONFIG_INFO_JSON_FILE_PATH = "configInfo.json"

/**
 * Class to work with project configInfo
 */
object ConfigInfoProvider {

    private val configInfoJsonFile = File(CONFIG_INFO_JSON_FILE_PATH)

    var globalConfigInfo: GlobalConfigInfo = parseProjectConfigInfoJson()

    fun incrementUnstableVersion() {
        globalConfigInfo = globalConfigInfo.copy(unstableVersion = globalConfigInfo.unstableVersion + 1)
        JsonHelper.write(ConfigInfoJson(globalConfigInfo), configInfoJsonFile)
    }

    fun incrementProjectSnapshotVersion() {
        globalConfigInfo = globalConfigInfo.copy(projectSnapshotVersion = globalConfigInfo.projectSnapshotVersion + 1)
        JsonHelper.write(ConfigInfoJson(globalConfigInfo), configInfoJsonFile)
    }

    private fun parseProjectConfigInfoJson(): GlobalConfigInfo = GsonBuilder().create()
            .fromJson(configInfoJsonFile.reader(), ConfigInfoJson::class.java)
            .transform()

    @JvmStatic
    private fun getVersion(): String = globalConfigInfo.version
}