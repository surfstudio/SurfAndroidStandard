package ru.surfstudio.android.build

import ru.surfstudio.android.build.exceptions.release_notes.ReleaseNotesFormatException
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.release_notes.ReleaseNotesInfo
import ru.surfstudio.android.build.model.release_notes.ReleaseNotesItem
import ru.surfstudio.android.build.model.release_notes.ReleaseNotesLibrary
import ru.surfstudio.android.build.model.release_notes.ReleaseNotesVersion

private const val NEXT_LINE = "\n"
private const val TOC = "[TOC]"
private const val TITLE_SIGN = "# "
private const val VERSION_SIGN = "## "
private const val LIBRARY_SIGN = "##### "
private const val ITEM_SIGN = "* "

/**
 * Helper to create release notes objects
 */
class ReleaseNotesParser {

    fun createReleaseNotes(component: Component, plainText: String) = ReleaseNotesInfo(
            component = component,
            toc = plainText.contains(TOC),
            title = getLine(plainText, TITLE_SIGN),
            versions = parseContent(
                    plainText,
                    VERSION_SIGN,
                    "version",
                    this::createReleaseNotesVersion
            )
    )

    private fun createReleaseNotesVersion(plainText: String) = ReleaseNotesVersion(
            version = plainText.substringBefore(NEXT_LINE),
            libraries = parseContent(
                    plainText,
                    LIBRARY_SIGN,
                    "library",
                    this::createReleaseNotesLibrary
            )
    )


    private fun createReleaseNotesLibrary(plainText: String) = ReleaseNotesLibrary(
            name = plainText.substringBefore(NEXT_LINE),
            items = parseContent(
                    plainText,
                    ITEM_SIGN,
                    "item",
                    this::createReleaseNotesItem
            )
    )


    private fun createReleaseNotesItem(plainText: String) = ReleaseNotesItem(
            content = plainText.substringAfter(ITEM_SIGN)
    )

    /**
     * Parse plainText and create model
     *
     * @param content - text to parse
     * @param sign - key that define different parts
     * @param modelName - model name to create
     * @param parseAction - creating
     */
    private fun <T> parseContent(content: String, sign: String, modelName: String, parseAction: (String) -> T): List<T> {
        val contentParts = content.split("^$sign".toRegex(RegexOption.MULTILINE))
        if (contentParts.size == 1) throw ReleaseNotesFormatException("ReleaseNotes doesn't have any $modelName \n $content")
        return contentParts.drop(1).map { parseAction(it) }
    }

    /**
     * Get string between sign and next line
     */
    private fun getLine(content: String, sign: String) = content.substringAfter(sign).substringBefore(NEXT_LINE)

}
