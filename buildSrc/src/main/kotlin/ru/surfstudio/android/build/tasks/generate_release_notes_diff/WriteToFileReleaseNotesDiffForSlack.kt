package ru.surfstudio.android.build.tasks.generate_release_notes_diff

/**
 * Task to see the differences between two revisions of RELEASE_NOTES.md in each module of a project
 * and send them to Slack.
 */
open class WriteToFileReleaseNotesDiffForSlack : WriteToFileReleaseNotesDiff() {

    override fun addLineChange(change: String) {
        super.addLineChange(styleLine(change))
    }

    private fun styleLine(line: String): String {
        val lineInfoMatch = LINE_INFO_REGEX.toRegex().find(line)
        if (lineInfoMatch?.value == null) return setBold(setRedBackground(line))

        var stylizedLine = ""
        var lineInfo = lineInfoMatch.value.trim()
        var lineText = line.substring(lineInfoMatch.range.last + 1).trim()

        val lineNumber = lineInfoMatch.groups[1]
        lineNumber?.let {
            lineInfo = lineInfo.replaceRange(lineNumber.range, "").trim()
        }

        val lineEditType = lineInfoMatch.groupValues[2]
        if (lineEditType == "-") stylizedLine = setQuote(stylizedLine)
        lineInfo = lineInfo.replaceFirst(lineEditType, "")

        val isStartParagraph = lineInfoMatch.groupValues[3].isNotBlank()
        if (isStartParagraph) {
            if (lineEditType == "+") {
                lineInfo = lineInfo.replaceFirst(lineInfoMatch.groupValues[3], SMILE_PLUS)
            } else {
                lineInfo = lineInfo.replaceFirst(lineInfoMatch.groupValues[3], "")
                lineInfo = SMILE_MINUS + lineInfo
            }
        }

        lineText = lineText.replace(
                NO_BACKWARD_LABEL_REGEX.toRegex(),
                SMILE_WARNING + setBold(setItalic(setRedBackground("NO BACKWARD COMPATIBILITY")))
        )

        stylizedLine += "$lineInfo $lineText"
        return stylizedLine
    }

    private fun setBold(text: String) = "*$text*"

    private fun setItalic(text: String) = "_${text}_"

    private fun setRedBackground(text: String) = "`$text`"

    private fun setQuote(text: String) = ">$text"

    companion object {
        const val LINE_INFO_REGEX = "([0-9]+) +([-+]) (\\*)?"
        const val NO_BACKWARD_LABEL_REGEX = "(\\*\\*)?NO BACKWARD COMPATIBILITY(\\*\\*)?"
        const val SMILE_WARNING = ":warning:"
        const val SMILE_PLUS = ":heavy_plus_sign:"
        const val SMILE_MINUS = ":heavy_minus_sign:"
    }
}