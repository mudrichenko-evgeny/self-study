package com.mudrichenko.evgeniy.selfstudy.data.model.`object`

enum class SortType {
    ASCENDING, DESCENDING;

    fun reverse(): SortType {
        return when (this) {
            ASCENDING -> DESCENDING
            DESCENDING -> ASCENDING
        }
    }

}