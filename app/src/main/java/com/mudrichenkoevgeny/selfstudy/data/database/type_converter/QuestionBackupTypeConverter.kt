package com.mudrichenkoevgeny.selfstudy.data.database.type_converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuestionBackup

class QuestionBackupTypeConverter {

    private val tokenType = object : TypeToken<List<QuestionBackup>>() {}.type

    @TypeConverter
    fun stringToType(value: String?): List<QuestionBackup> {
        value ?: return emptyList()
        return Gson().fromJson(value, tokenType)
    }

    @TypeConverter
    fun typeToString(type: List<QuestionBackup>): String? {
        val gson = Gson()
        return gson.toJson(type, tokenType)
    }

}