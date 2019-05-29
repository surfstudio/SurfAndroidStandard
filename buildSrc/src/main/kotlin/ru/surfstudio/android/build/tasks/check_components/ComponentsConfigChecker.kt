package ru.surfstudio.android.build.tasks.check_components

import com.beust.klaxon.Klaxon
import groovy.util.ConfigObject
import groovy.util.ConfigSlurper
import org.gradle.api.GradleException
import ru.surfstudio.android.build.model.Component
import java.io.File
import java.lang.RuntimeException

//TODO: рефакторинг
class ComponentsConfigChecker(val firstRevision: String,
                              val secondRevision: String
) : ComponentsChecker {

    private val tempFolderName = "temp"
    private val tempDirectory = "$currentDirectory//$tempFolderName"
    private val changesFileName = "changes.txt"
    private val COMPONENTS_JSON_FILE_PATH = "/buildSrc/components.json"
    private val CONFIG_FILE_PATH = "/buildSrc/config.gradle"

    override fun getChangedComponents(): List<Component> {
        checkoutGitRevision(firstRevision, currentDirectory)
        createTempFolder()
        copyProjectToTempFolder(currentDirectory, tempDirectory)
        checkoutGitRevision(secondRevision, tempDirectory)
        createProjectInfoFile(currentDirectory)
        createProjectInfoFile(tempDirectory)
        return emptyList()
    }

    private fun copyProjectToTempFolder(from: String, to : String){
        val fileFrom = File(from)
        fileFrom.listFiles().forEach {
            if (it.name != tempFolderName) {
                it.copyRecursively(File(to + "/" + it.name), true) { file, ioException ->
                    throw GradleException(ioException.message)
                }
            }
        }
    }

    private fun createTempFolder(){
        if (!File(tempDirectory).exists()){
            File(tempDirectory).mkdir()
        }
    }

    private fun checkoutGitRevision(revision: String, currentDirectory: String){
        GitExecutor(currentDirectory).checkoutRevision(revision)
    }

    private fun createProjectInfoFile(dir: String){
        if (!File("$dir//$changesFileName").exists()){
            File("$dir//$changesFileName").createNewFile()
        }
        createProjectInfo(dir)
    }

    private fun createProjectInfo(dir: String){
        val components = parseComponentJson(dir + COMPONENTS_JSON_FILE_PATH)
        val config = parseConfigFile(dir + CONFIG_FILE_PATH)
        val allList = config.getValue("ext") as ConfigObject
        val libraries = allList.get("libraryVersions") as LinkedHashMap<String, String>
        //TODO : сопоставить компоненты и версии их библиотек. Сериализовать в файл
    }

    private fun addTempFolderToGitIgnore(){
        val gitIgnore = ".gitignore"
        val fileGitIgnore = File("$currentDirectory/$gitIgnore")
        val textGitIgnore = fileGitIgnore.readText()
        if (!textGitIgnore.contains("/$tempFolderName")){
            fileGitIgnore.appendText("/$tempFolderName")
        }
    }

    private fun parseComponentJson(path: String): List<Component> {
        return Klaxon().parseArray(File(path))
                ?: throw RuntimeException("Can't parse components.json")
    }

    private fun parseConfigFile(path: String):ConfigObject{
        val file = File(path)
        return ConfigSlurper().parse(file.readText())
    }

}