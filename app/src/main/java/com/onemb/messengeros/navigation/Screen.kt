package com.onemb.messengeros.navigation

sealed class Screen(val route: String) {
    data object Permission : Screen(route = "permission_screen")
    data object Main : Screen(route = "main_screen")

    data object Search : Screen(route = "search_screen")

    data object NewConversation : Screen(route = "conversation_screen")

    data object Conversation : Screen(route = "message_detail") {
        const val ARG_CONVERSATION = "conversationArgs"
        const val ARGUMENT_NAME = "senderName"
    }
}
