package ru.surfstudio.android.build.tasks.check_components

import java.io.File


class GitExecutor(

) {

    private val GIT_DIFF_COMMAND = "git diff --no-commit-id --name-only"
    private val GIT_CHECKOUT_COMMAND = "git checkout"
    private val SPLIT_STRING = "\\s"


    fun diff(firstRevision: String, secondRevision: String): List<String>? {
        val command = "$GIT_DIFF_COMMAND $firstRevision $secondRevision"
        val res = CommandLineRunner().runCommandWithResult(command, File(currentDirectory))
        return res?.split(SPLIT_STRING.toRegex())
    }

    fun checkoutRevision(revision: String) {
        val command = "$GIT_CHECKOUT_COMMAND $revision"
        val res =CommandLineRunner().runCommandWithResult(command, File(currentDirectory))
        println(res)
    }
}