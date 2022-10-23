package com.mudrichenko.evgeniy.selfstudy.domain.file_repository

import android.content.Context
import android.content.res.AssetManager
import android.net.Uri
import android.provider.OpenableColumns
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.AppError
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.DataResponse
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizPack
import java.io.*
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

    override fun getDefaultQuizPackNames(): List<String> {
        return getFileNamesFromAssets(DIRECTORY_ASSET_QUIZ_PACKS)
    }

    private fun getFileNamesFromAssets(directory: String?): List<String> {
        return try {
            getAssets().list(directory ?: "")?.toList() ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override fun createQuizPackFromAssets(
        fileName: String
    ): File? {
        return createFileFromAssets(
            directory = DIRECTORY_ASSET_QUIZ_PACKS,
            fileName = fileName,
            outDirectory = DIRECTORY_DEFAULT_QUIZ_PACKS,
            outFileName = null
        )
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
        return csvReader().readAllWithHeader(file)
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