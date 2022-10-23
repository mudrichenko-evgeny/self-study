package com.mudrichenko.evgeniy.selfstudy.data.database.type_converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.AnswerStatus

class AnswerStatusTypeConverter {

    private val tokenType = object : TypeToken<AnswerStatus>() {}.type

    @TypeConverter
    fun stringToType(value: String?): AnswerStatus? {
        value ?: return null
        return Gson().fromJson(value, tokenType)
    }

    @TypeConverter
    fun typeToString(type: AnswerStatus?): String? {
        type ?: return null
        val gson = Gson()
        return gson.toJson(type, tokenType)
    }

}