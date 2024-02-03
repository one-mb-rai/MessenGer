package com.onemb.messengeros.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.onemb.messengeros.model.ConversationArgs
import com.onemb.messengeros.pages.ConversationListScreen
import com.onemb.messengeros.pages.MessagesListScreen
import com.onemb.messengeros.pages.PermissionScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Permission.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Permission.route) {
            PermissionScreen(
                onPermissionGranted = {
                    navController.popBackStack()
                    navController.navigate(Screen.Main.route)
                }
            )
        }

        composable(route = Screen.Main.route) {
            MessagesListScreen (navController = navController)
        }


        composable(route = Screen.Conversation.route + "/{senderName}") { backStackEntry ->
            val conversationArgs = ConversationArgs(backStackEntry.arguments?.getString(Screen.Conversation.ARGUMENT_NAME))
            ConversationListScreen(navController, conversationArgs)
        }
    }
}