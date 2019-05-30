package ru.surfstudio.android.build.model

/**
 * Представляет информацию о зависимости с ее версией
 */
data class DepWithVersion(
        val name: String = "",
        var version: String = ""
) {
    override fun equals(other: Any?): Boolean {
        val dep = other as DepWithVersion
        return dep.version == this.version
    }
}