package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.model.module.Library
import ru.surfstudio.android.build.model.module.Module
import ru.surfstudio.android.build.model.module.Sample
import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Represent information about component
 */
data class Component(
        val name: String = EMPTY_STRING,
        val directory: String = EMPTY_STRING,
        val baseVersion: String = EMPTY_STRING, // Version from components.json
        var projectVersion: String = EMPTY_STRING, // Project version
        val libraries: List<Library> = listOf(),
        val samples: List<Sample> = listOf(),
        val disabled: Boolean = false,
        val enabledSamples: Boolean = false,
        val hasMirror: Boolean = false,
        val mirrorRepo: String = EMPTY_STRING
) {

    /**
     * Get components module
     */
    fun getModules(skipSamples: Boolean = false): List<Module> =
            if (skipSamples)
                libraries
            else
                libraries + samples
}