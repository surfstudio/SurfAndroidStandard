package ru.surfstudio.android.build.tasks.check_components

import com.beust.klaxon.Klaxon
import org.gradle.api.GradleException
import ru.surfstudio.android.build.model.Component
import java.io.File
import java.lang.RuntimeException

class ComponentsConfigChecker(val firstRevision: String,
                              val secondRevision: String
) : ComponentsChecker {

    val tempFolder = "temp"
    val buildSrc = "buildSrc"
    val tempDirectory = "$currentDirectory/$tempFolder"
    val changesFile = "changes.txt"

    override fun getChangedComponents(): List<Component> {
        checkoutGitRevision(firstRevision, currentDirectory)
        createChangeInfoFile(currentDirectory)
        createTempFolder()
        copyProjectFolder(currentDirectory, tempDirectory)
        checkoutGitRevision(secondRevision, tempDirectory)
        createChangeInfoFile(tempDirectory)
        return emptyList()
    }

    private fun copyProjectFolder(from: String, to : String){
        val fileFrom = File(from)
        val fileTo = File(to)
        fileFrom.walk().forEach {
            println("copy ${it.name}")
            if (it.name != to) {
                it.copyRecursively(fileTo, false) { file, ioException ->
                    throw GradleException("")
                }
            }
        }
    }

    private fun createTempFolder(){
        if (!File(tempDirectory).exists()){
            File(tempDirectory).createNewFile()
        }
    }

    private fun addTempFolderToGitIgnore(){
        val gitIgnore = ".gitignore"
        val fileGitIgnore = File("$currentDirectory/$gitIgnore")
        val textGitIgnore = fileGitIgnore.readText()
        if (!textGitIgnore.contains("/$tempFolder")){
            fileGitIgnore.writeText("/$tempFolder")
        }
    }

    private fun checkoutGitRevision(revision: String, currentDirectory: String){
        GitExecutor(currentDirectory).checkoutRevision(revision)
    }

    private fun createChangeInfoFile(dir: String){
        if (!File("$dir/changesFile.txt").exists()){
            File("$dir/changesFile.txt").createNewFile()
        }
    }

    private fun createChangeInfo(dir: String){
        val components = parseComponentJson(dir)

    }

    private fun compareChangeFiles(firstDirectory: String, secondDirectory: String){

    }

    private fun parseComponentJson(path: String): List<Component> {
        return Klaxon().parseArray(File(path))
                ?: throw RuntimeException("Can't parse components.json")
    }

}