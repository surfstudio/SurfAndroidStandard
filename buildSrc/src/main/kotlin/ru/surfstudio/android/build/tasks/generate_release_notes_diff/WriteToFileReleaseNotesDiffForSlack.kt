package ru.surfstudio.android.build.tasks.generate_release_notes_diff

/**
 * Task to see the differences between two revisions of RELEASE_NOTES.md in each module of a project.
 */
open class WriteToFileReleaseNotesDiffForSlack : WriteToFileReleaseNotesDiff() {

    companion object {
        //stylized line const
        const val LINE_INFO_REGEX = "([0-9]+) +([-+]) (\\*)?"
        const val NO_BACKWARD_LABEL_REGEX = "(\\*\\*)?NO BACKWARD COMPATIBILITY(\\*\\*)?"
        const val SMILE_WARNING = ":warning:"
        const val SMILE_CHECK_MARK = ":heavy_check_mark:"
        const val SMILE_DELETE = ":small_red_triangle_down:"
    }

    override fun addLineChange(change: String) {
        super.addLineChange(styleLine(change))
    }

    private fun styleLine(line: String): String {
        val matchResult = LINE_INFO_REGEX.toRegex().find(line)
        if (matchResult?.value == null) return setBold(setRedBackground(line))

        var stylizedLine = ""
        var lineInfo = matchResult.value.trim()
        var lineText = line.substring(matchResult.range.last + 1).trim()

        val rawEditType = matchResult.groupValues[2]
        if (rawEditType == "-") stylizedLine = setQuote(stylizedLine)
        lineInfo = lineInfo.replace(rawEditType, "")

        val isStartParagraph = matchResult.groupValues[3].isNotBlank()
        if (isStartParagraph) {
            if (rawEditType == "+") {
                lineInfo = lineInfo.replace(matchResult.groupValues[3], SMILE_CHECK_MARK)
            } else {
                lineInfo = lineInfo.replace(matchResult.groupValues[3], "")
                lineInfo = SMILE_DELETE + lineInfo
            }
        }

        lineText = lineText.replace(
                NO_BACKWARD_LABEL_REGEX.toRegex(),
                SMILE_WARNING + setBold(setItalic(setRedBackground("NO BACKWARD COMPATIBILITY")))
        )
        stylizedLine += lineInfo + lineText

        return stylizedLine
    }

    private fun setBold(text: String) = "*$text*"

    private fun setItalic(text: String) = "_${text}_"

    private fun setRedBackground(text: String) = "`$text`"

    private fun setQuote(text: String) = ">$text"
}