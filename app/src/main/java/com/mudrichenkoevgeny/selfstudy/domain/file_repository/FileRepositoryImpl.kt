package com.mudrichenkoevgeny.selfstudy.domain.file_repository

import android.content.Context
import android.content.res.AssetManager
import android.net.Uri
import android.provider.OpenableColumns
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.AppError
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.DataResponse
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuizPack
import java.io.*
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.Exception

@Suppress("SameParameterValue")
class FileRepositoryImpl(
    private val app: Context
): FileRepository {

    companion object {
        const val DIRECTORY_TEMP_FILES = "temp"
        const val DIRECTORY_AUDIO_RECORDS = "records"
        const val DIRECTORY_ASSET_QUIZ_PACKS = "quiz_packs"
        const val DIRECTORY_USER_QUIZ_PACKS = "quiz_packs_user"
        const val DIRECTORY_DEFAULT_QUIZ_PACKS = "quiz_packs_default"
        const val DIRECTORY_DATABASE_BACKUP_QUESTION = "backup_question"
    }

    override fun convertContentUriToFile(uri: Uri?): File? {
        try {
            val inputStream = app.contentResolver.openInputStream(uri ?: return null)
                ?: return null
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)

            val fileName = getDisplayFileName(uri)
            val dir = getTempDir()
            val targetFile = File(dir, fileName)

            val outputStream: OutputStream = FileOutputStream(targetFile)
            outputStream.write(buffer)

            return targetFile
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun makeDirectory(pathName: String): File {
        return getDirectory(pathName)
    }

    override fun deleteDirectory(pathName: String) {
        val dir = File(pathName)
        dir.deleteRecursively()
    }

    override fun copyFile(filePath: String, outDirectory: String, outFileName: String): File? {
        return saveFile(
            inputStream = File(filePath).inputStream(),
            file = File(getDirectory(outDirectory), outFileName)
        )
    }

    private fun getDirectory(pathName: String): File {
        val dir = File(pathName)
        if (!dir.exists())
            dir.mkdir()
        return dir
    }

    private fun getDisplayFileName(uri: Uri): String {
        var name = "file_${System.currentTimeMillis()}"
        if (uri.scheme == "content") {
            val cursor = app.contentResolver.query(
                uri,
                arrayOf(OpenableColumns.DISPLAY_NAME),
                null,
                null,
                null
            )
            cursor.use {
                it?.let {
                    if (it.moveToFirst()) {
                        it.getColumnIndex(OpenableColumns.DISPLAY_NAME).let { index ->
                            if (index >= 0) {
                                try {
                                    name = it.getString(index)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
            }
        }
        return name
    }

    override fun getQuizPacksNamesFromAssets(): List<String> {
        return getFileNamesFromAssetsDirectory(DIRECTORY_ASSET_QUIZ_PACKS)
    }

    private fun getFileNamesFromAssetsDirectory(directory: String?): List<String> {
        return try {
            getAssets().list(directory ?: "")?.toList() ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override fun cacheQuizPackFromAssets(
        fileName: String
    ): File? {
        return createFileFromAssets(
            directory = DIRECTORY_ASSET_QUIZ_PACKS,
            fileName = fileName,
            outDirectory = null,
            outFileName = null
        )
    }

    override fun saveQuizPackFromAssets(file: File): File? {
        val targetFile = File(
            getDirectory("${getDataDirectory()}/$DIRECTORY_DEFAULT_QUIZ_PACKS"),
            file.name
        )

        return try {
            targetFile.parentFile?.mkdirs()

            file.copyTo(targetFile, overwrite = true)

            if (!file.delete()) {
                throw IOException("Can`t delete file: ${file.absolutePath}")
            }

            targetFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun getFileMD5(file: File): String {
        val md = MessageDigest.getInstance("MD5")
        val buffer = ByteArray(8192)
        val inputStream = file.inputStream()

        try {
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                md.update(buffer, 0, bytesRead)
            }
        } finally {
            inputStream.close()
        }

        val digest = md.digest()
        val bigInt = BigInteger(1, digest)
        return bigInt.toString(16).padStart(32, '0')
    }

    private fun createFileFromAssets(
        directory: String?,
        fileName: String,
        outDirectory: String?,
        outFileName: String?
    ): File? {
        try {
            val filePath = if (directory != null && directory.isNotEmpty())
                "$directory/$fileName"
            else
                fileName
            getAssets().open(filePath).let { inputStream ->
                val dir = if (outDirectory == null)
                    getTempDir()
                else
                    getDirectory("${getDataDirectory()}/$outDirectory")
                val targetFile = File(dir, outFileName ?: fileName)
                return saveFile(
                    inputStream = inputStream,
                    file = targetFile
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun getAssets(): AssetManager {
        return app.assets
    }

    private fun saveFile(inputStream: InputStream, file: File): File? {
        return try {
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            val outputStream: OutputStream = FileOutputStream(file)
            outputStream.write(buffer)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun getRowsFromCsvFile(file: File): List<Map<String, String>> {
        val delimiters = listOf(',', ';')

        for (delimiter in delimiters) {
            try {
                return readCsvWithDelimiter(file, delimiter)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return emptyList()
    }

    private fun readCsvWithDelimiter(file: File, delimiterChar: Char): List<Map<String, String>> {
        return csvReader {
            delimiter = delimiterChar
        }.readAllWithHeader(file)
    }

    override fun saveRowsIntoCsv(file: File?, rows: List<List<String>>): DataResponse<Unit> {
        if (file == null) return DataResponse.Error(AppError.QUESTION_PACK_EDIT)
        csvWriter().writeAll(
            rows = rows,
            targetFile = file,
            append = false
        )
        return DataResponse.Successful(Unit)
    }

    override fun writeTextToFile(directory: String, fileName: String, text: String): File? {
        try {
            getDirectory(directory)
            val file = File(getDirectory(directory), fileName)
            val writer = FileWriter(file)
            writer.write(text)
            writer.flush()
            writer.close()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun readTextFromFile(directory: String, fileName: String): String? {
        try {
            val file = File(getDirectory(directory), fileName)
            val reader = FileReader(file)
            val text = reader.readText()
            reader.close()
            return text
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun getFile(directory: String, fileName: String): File? {
        val dir = File(directory)
        if (!dir.exists()) return null
        val file = File(directory, fileName)
        if (!file.exists()) return null
        return file
    }

    override fun deleteFile(directory: String, fileName: String) {
        deleteFile(File(getDirectory(directory), fileName))
    }

    private fun deleteFile(file: File) {
        if (file.exists()) {
            file.delete()
        }
    }

    override fun getDataDirectory(): File {
        return getDirectory("${app.dataDir}")
    }

    override fun getTempDir(): File {
        return getDirectory("${app.cacheDir}/$DIRECTORY_TEMP_FILES")
    }

    override fun getBackupDirectory(): String {
        return "${getDataDirectory().path}/${DIRECTORY_DATABASE_BACKUP_QUESTION}"
    }

    override fun getPacksDirectory(quizPack: QuizPack): String {
        return if (quizPack.isUserPack)
            getUserPacksDirectory()
        else
            getDefaultPacksDirectory()
    }

    override fun getDefaultPacksDirectory(): String {
        return "${getDataDirectory().path}/${DIRECTORY_DEFAULT_QUIZ_PACKS}"
    }

    override fun getUserPacksDirectory(): String {
        return "${getDataDirectory().path}/${DIRECTORY_USER_QUIZ_PACKS}"
    }

    override fun getAudioRecordsDirectory(): String {
        return "${getDataDirectory().path}/${DIRECTORY_AUDIO_RECORDS}"
    }

}