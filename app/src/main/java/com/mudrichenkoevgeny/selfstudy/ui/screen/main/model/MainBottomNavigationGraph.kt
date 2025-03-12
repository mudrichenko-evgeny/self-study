package com.mudrichenkoevgeny.selfstudy.ui.screen.main.model

import com.mudrichenkoevgeny.selfstudy.R

enum class MainBottomNavigationGraph {
    HOME,
    QUIZ_PACKS;

    fun getGraphId(): Int {
        return when(this) {
            HOME -> R.id.nav_graph_home
            QUIZ_PACKS -> R.id.nav_graph_quiz_packs
        }
    }

}