package com.mudrichenkoevgeny.selfstudy.system.model

@Suppress("unused")
enum class MimeFileType(val key: String) {
    ALL("*/*"),
    IMAGE("image/*");

    companion object {
        fun getByKey(key: String?): MimeFileType? {
            return values().firstOrNull { it.key == key }
        }
    }

}