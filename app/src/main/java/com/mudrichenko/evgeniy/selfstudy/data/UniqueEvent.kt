package com.mudrichenko.evgeniy.selfstudy.data

import kotlin.random.Random

class UniqueEvent {

    override fun equals(other: Any?): Boolean {
        return false
    }

    override fun hashCode(): Int {
        return Random.nextInt()
    }

}