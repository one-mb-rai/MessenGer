package com.onemb.messengeros.pages

import SenderView
import SmsViewModel
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.onemb.messengeros.model.SMSMessage
import com.onemb.messengeros.navigation.Screen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onemb.messengeros.model.ConversationArgs
import kotlinx.coroutines.flow.StateFlow


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesListScreen(navController: NavHostController, viewModel: SmsViewModel = viewModel()) {
    val allMessages = viewModel.smsList
    val darkTheme: Boolean = isSystemInDarkTheme()


    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
            ) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if(darkTheme) Color(0xFF2B201D) else Color(0xFFF9F2F5),
                        titleContentColor = if(darkTheme) Color(0xFFF9F2F5) else Color(0xFF2B201D),
                    ),
                    title = {
                        Text(
                            "Messages",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                            IconButton(onClick = { /* do something */ }) {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = "Search Message",
                                )
                        }
                    },
                )
            }
        },
    ) { innerPadding -> 
            if(allMessages.value.isNotEmpty()) {
                messageList(allMessages, innerPadding, navController)
            } else {
                Text(text = "Loading ...")    
            }
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun messageList(allMessages: StateFlow<Map<String, List<SMSMessage>>>, innerPadding: PaddingValues, navController: NavHostController) {
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
        items(
            count = allMessages.value.size,
            key = {
                allMessages.value.toList()[it]
            },
            itemContent = {index ->
                val dataIndex = allMessages.value.toList()[index].first
                val dataValue = allMessages.value.toList()[index].second
                SenderListItem(sender = dataIndex, messages = dataValue, navController)
            }
        )
    }
}

@Composable
fun SenderListItem(sender: String, messages: List<SMSMessage>, navController: NavHostController) {

    Column(
        modifier = Modifier
            .height(70.dp)
            .clickable {
                val conversationArgs = ConversationArgs(sender)
                navController.navigate(Screen.Conversation.route + "/${conversationArgs.senderName}")
                Log.d("MyApp", "Sending ConversationArgs: $conversationArgs")

            },
    ) {
        SenderView(sender = sender, messages = messages)
    }
}
