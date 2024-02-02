package com.onemb.messengeros.pages

import SenderView
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.onemb.messengeros.model.SMSMessage
import com.onemb.messengeros.navigation.Screen
import com.onemb.messengeros.model.readMessages

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesListScreen(navController: NavHostController) {
    val context = LocalContext.current
    val allMessages = remember { mutableStateMapOf<String, List<SMSMessage>>() }
    val darkTheme: Boolean = isSystemInDarkTheme()

    LaunchedEffect(key1 = Unit) {
        val messages = readMessages(context = context, type = "inbox") + readMessages(
            context = context,
            type = "all"
        )
        allMessages += messages.sortedByDescending { it.date }.groupBy { it.sender }
    }

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
            if(allMessages.size > 0) {
                messageList(allMessages, innerPadding, navController)
            } else {
                Text(text = "Loading ...")    
            }
    }
}


@Composable
fun messageList(allMessages: SnapshotStateMap<String, List<SMSMessage>>, innerPadding: PaddingValues, navController: NavHostController) {
    val allMessagesList = remember { mutableStateOf(allMessages.entries.sortedByDescending { entry -> entry.value.maxOfOrNull { it.date } ?: 0})}
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
            count = allMessagesList.value.size,
            key = {
                allMessagesList.value.toList()[it].key
            },
            itemContent = {index ->
                val dataIndex = allMessagesList.value.toList()[index].key
                val dataValue = allMessagesList.value.toList()[index].value
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
                navController.navigate("${Screen.Conversation.route}/${sender}")
            },
    ) {
        SenderView(sender = sender, messages = messages)
    }
}
