package com.mudrichenkoevgeny.selfstudy.domain.file_repository

import android.net.Uri
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.DataResponse
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuizPack
import java.io.File

interface FileRepository {

    fun makeDirectory(pathName: String): File
    fun deleteDirectory(pathName: String)

    fun convertContentUriToFile(uri: Uri?): File?
    fun getDefaultQuizPackNames(): List<String>
    fun createQuizPackFromAssets(fileName: String): File?

    fun getRowsFromCsvFile(file: File): List<Map<String, String>>
    fun saveRowsIntoCsv(file: File?, rows: List<List<String>>): DataResponse<Unit>

    fun writeTextToFile(directory: String, fileName: String, text: String): File?
    fun readTextFromFile(directory: String, fileName: String): String?

    fun getFile(directory: String, fileName: String): File?
    fun deleteFile(directory: String, fileName: String)
    fun copyFile(filePath: String, outDirectory: String, outFileName: String): File?

    fun getDataDirectory(): File
    fun getTempDir(): File
    fun getBackupDirectory(): String
    fun getPacksDirectory(quizPack: QuizPack): String
    fun getDefaultPacksDirectory(): String
    fun getUserPacksDirectory(): String
    fun getAudioRecordsDirectory(): String

}