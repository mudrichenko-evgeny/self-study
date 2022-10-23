package com.mudrichenko.evgeniy.selfstudy.ui.adapter.quiz_pack

import androidx.lifecycle.MutableLiveData
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizPack
import kotlin.random.Random

sealed class QuizPackAdapterItemVM {

    abstract fun getId(): Long

    companion object {
        const val QUIZ_PACK = 1
        const val QUIZ_PACK_ACTIVE = 2
        const val HELP = 3
    }

    abstract fun getAdapterItemType(): Int

    class QuizPackItem(
        val quizPack: QuizPack
    ): QuizPackAdapterItemVM() {

        override fun getAdapterItemType() = QUIZ_PACK

        override fun getId(): Long = quizPack.id

    }

    class QuizPackItemActive(
        val quizPack: QuizPack
    ): QuizPackAdapterItemVM() {

        override fun getAdapterItemType() = QUIZ_PACK_ACTIVE

        override fun getId(): Long = quizPack.id

    }

    class Help(
        private val itemId: Long = Random.nextLong(),
        val expanded: MutableLiveData<Boolean> = MutableLiveData(false)
    ) : QuizPackAdapterItemVM() {

        override fun getAdapterItemType() = HELP

        override fun getId(): Long = itemId

    }

}