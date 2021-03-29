package ru.surfstudio.android.navigation.observer.storage.file

import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import ru.surfstudio.android.navigation.observer.storage.RouteStorage
import java.io.*
import java.nio.ByteBuffer

class FileRouteStorage(
    rootDirectoryName: String
) : RouteStorage {

    private val rootDirectory: File

    init {
        rootDirectory = File(rootDirectoryName, SCREEN_FOR_ROUTE_DIR_NAME)
        rootDirectory.mkdir()
    }

    override fun <T : Serializable> get(): ActivityWithResultRoute<T>? {
        val bytes = getBytesFromFile(FILE_NAME)
        return bytes?.let { decode(it) }
    }

    override fun <T : Serializable> save(info: ActivityWithResultRoute<T>) {
        val bytes = encode(info) ?: return
        saveBytesOrRewrite(FILE_NAME, bytes)
    }

    override fun hasValue(): Boolean {
        return getFilesList()
            .any { it.name == FILE_NAME }
    }

    override fun clear() {
        getFilesList()
            .filter { it.canRead() && it.canWrite() }
            .forEach { it.delete() }
    }

    private fun getFilesList(): Array<File> {
        return rootDirectory.listFiles() ?: emptyArray()
    }

    private fun getBytesFromFile(id: String): ByteArray? {
        return try {
            getBytesFromFileUnsafe(id)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun getBytesFromFileUnsafe(id: String): ByteArray? {
        if (containsFile(id)) {
            val file = File(rootDirectory, id)
            var buffer: ByteBuffer
            RandomAccessFile(file, READ_MODE).use { randomAccessFile ->
                randomAccessFile.channel.use { fileChannel ->
                    val fileSize: Long = fileChannel.size()
                    buffer = ByteBuffer.allocate(fileSize.toInt())
                    fileChannel.read(buffer)
                    buffer.flip()
                    return buffer.array()
                }
            }
        }
        return null
    }

    private fun saveBytesOrRewrite(key: String, encode: ByteArray) {
        try {
            saveBytesOrRewriteUnsafe(key, encode)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun saveBytesOrRewriteUnsafe(key: String, encode: ByteArray) {
        val file = File(rootDirectory, key)
        file.delete()
        RandomAccessFile(file, READ_WRITE_MODE).use { randomAccessFile ->
            randomAccessFile.channel.use { fileChannel ->
                val buffer = ByteBuffer.allocate(encode.size)
                buffer.put(encode)
                buffer.flip()
                fileChannel.write(buffer)
            }
        }
    }

    private fun containsFile(key: String): Boolean {
        return File(rootDirectory, key).exists()
    }

    private fun <T : Serializable> encode(value: T): ByteArray? {
        try {
            ByteArrayOutputStream().use { byteArrayOutputStream ->
                ObjectOutputStream(byteArrayOutputStream).use { objectOutputStream ->
                    objectOutputStream.writeObject(value)
                    return byteArrayOutputStream.toByteArray()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    private fun <T : Serializable> decode(rawValue: ByteArray): T? {
        try {
            ByteArrayInputStream(rawValue).use { byteArrayInputStream ->
                ObjectInputStream(byteArrayInputStream).use { objectInputStream ->
                    return objectInputStream.readObject() as T
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            return null
        }
    }

    private companion object {
        const val READ_MODE = "r"
        const val READ_WRITE_MODE = "rw"
        const val SCREEN_FOR_ROUTE_DIR_NAME = "screen_route_storage"
        const val FILE_NAME = "route_storage"
    }
}