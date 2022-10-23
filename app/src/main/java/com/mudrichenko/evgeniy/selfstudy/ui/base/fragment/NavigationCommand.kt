package com.mudrichenko.evgeniy.selfstudy.ui.base.fragment

sealed class NavigationCommand {

    class NavigateTo(val direction: Direction): NavigationCommand() {

        sealed class Direction {

            sealed class Screen: Direction() {

                object Home: Screen()

                object Quiz: Screen()

                object QuizCompleted: Screen()

                class CheckAnswer(val questionId: Long): Screen()

                object QuestionPackEdit: Screen()

                object Statistics: Screen()

            }

            sealed class Dialog: Direction() {

                class QuestionStatistics(val questionId: Long): Dialog()

                class EditQuestion(val quizPackId: Long, val questionId: Long): Dialog()

            }

        }

    }

    object Back: NavigationCommand()

}