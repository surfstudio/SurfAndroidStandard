package ru.surfstudio.android.build.utils

import com.google.gson.GsonBuilder
import ru.surfstudio.android.build.model.json.ComponentJson
import java.io.File

/**
 * Helper class for parsing
 */
object JsonHelper {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    /**
     * Write to file with json formatting
     *
     * @param obj - object for represent as json
     * @param file - file to write
     */
    fun write(obj: Any, file: File) {
        file.writeText(gson.toJson(obj))
    }

    fun parseComponentsJson(filePath: String): List<ComponentJson> {
        return GsonBuilder()
                .create()
                .fromJson(File(filePath).reader(), Array<ComponentJson>::class.java)
                .toList()
    }
}