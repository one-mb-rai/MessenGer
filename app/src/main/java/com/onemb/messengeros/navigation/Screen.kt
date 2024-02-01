package com.onemb.messengeros.navigation

sealed class Screen(val route: String) {
    object Permission : Screen(route = "permission_screen")
    object Main : Screen(route = "main_screen")

    object Conversation : Screen(route = "message_detail") {
        const val ARG_CONVERSATION = "conversationArgs"
        const val argumentName = "senderName"
    }
}
