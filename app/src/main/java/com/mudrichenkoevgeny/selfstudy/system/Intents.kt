package com.mudrichenkoevgeny.selfstudy.system

import android.content.Intent
import com.mudrichenkoevgeny.selfstudy.system.model.MimeFileType

object Intents {

    fun pick(mimeFileType: MimeFileType): Intent {
        return Intent(Intent.ACTION_GET_CONTENT).apply {
            type = mimeFileType.key
            addCategory(Intent.CATEGORY_DEFAULT)
        }
    }

}