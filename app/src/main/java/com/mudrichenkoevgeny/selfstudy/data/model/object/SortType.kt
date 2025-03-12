package com.mudrichenkoevgeny.selfstudy.data.model.`object`

enum class SortType {
    ASCENDING, DESCENDING;

    fun reverse(): SortType {
        return when (this) {
            ASCENDING -> DESCENDING
            DESCENDING -> ASCENDING
        }
    }

}