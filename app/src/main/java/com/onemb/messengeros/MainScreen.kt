package com.onemb.messengeros

import SenderView
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.onemb.messengeros.components.TopBar
import com.onemb.messengeros.model.SMSMessage
import com.onemb.messengeros.navigation.Screen

@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val allMessages = remember { mutableStateMapOf<String, List<SMSMessage>>() }

    LaunchedEffect(key1 = Unit) {
        val messages =
            readMessages(context = context, type = "inbox") + readMessages(
                context = context,
                type = "all"
            )
        allMessages += messages.sortedByDescending { it.date }.groupBy { it.sender }
    }
    TopBar(allMessages, "Messages", true, navController)

}


@Composable
fun messageList(allMessages: SnapshotStateMap<String, List<SMSMessage>>, innerPadding: PaddingValues, navController: NavHostController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = innerPadding.calculateTopPadding() + 5.dp,
                start = 10.dp,
                end = 10.dp,
                bottom = 10.dp
            )
    ) {
        allMessages.entries.sortedByDescending { entry ->
            entry.value.maxOfOrNull { it.date } ?: 0
        }.forEach { (sender, messages) ->
            item(key = sender) {
                SenderListItem(sender = sender, messages = messages, navController)
            }
        }
    }
}

@Composable
fun SenderListItem(sender: String, messages: List<SMSMessage>, navController: NavHostController) {
    Column(
        modifier = Modifier
            .height(70.dp)
            .clickable {
                navController.navigate("${Screen.Conversation.route}/${sender}")
            },
    ) {
        SenderView(sender = sender, messages = messages)
    }
}
