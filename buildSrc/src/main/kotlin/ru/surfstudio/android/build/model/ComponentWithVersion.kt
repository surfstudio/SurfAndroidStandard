package ru.surfstudio.android.build.model

/**
 * Represent information about component with library versions
 */
class ComponentWithVersion(
        val id: String,
        val version: String,
        val isStable: Boolean,
        val libs: List<LibWithVersion> = listOf()
) {
}