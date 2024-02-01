package com.onemb.messengeros

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import com.onemb.messengeros.components.MessageView
import com.onemb.messengeros.model.SMSMessage
import com.onemb.messengeros.model.parsedDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ConversationScreen(args: NavHostController, senderName: String?) {

    val context = LocalContext.current
    val allMessages = remember { mutableStateMapOf<String, List<SMSMessage>>() }
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = Unit) {
        val messages =
            readMessages(context = context, type = "inbox") + readMessages(
                context = context,
                type = "all"
            )
        allMessages += messages.sortedBy { it.date }.groupBy { it.sender }
        listState.scrollToItem(allMessages.values.flatten().size - 1)
    }

    fun filterMessagesBySenderName(
        messagesMap: SnapshotStateMap<String, List<SMSMessage>>,
        senderNameToFilter: String
    ): Map<String, List<SMSMessage>> {
        return messagesMap.filterValues { messagesList ->
            messagesList.any { it.sender == senderNameToFilter }
        }
    }

// Example usage:


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                ),
                title = {
                    if (senderName != null) {
                        Text(
                            senderName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
//        senderName?.let { Text(modifier = Modifier.padding(innerPadding), text = it) }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface).padding(innerPadding),
            state = listState
        ) {
            senderName?.let {
                filterMessagesBySenderName(
                    allMessages,
                    it
                )
            }?.forEach { (_, messages) ->
                messages.groupBy { it.date.parsedDate().split(" ").first() }
                    .forEach { (date, smsMessage) ->
                        item {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .alpha(0.38f),
                                text = date,
                                textAlign = TextAlign.Center,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        items(
                            items = smsMessage,
                            key = { it.date }
                        ) {
                            MessageView(message = it)
                        }
                    }
            }
        }
    }

//    MessageView(message = smsMessage)
//    TopBar(allMessages, {}, "Messages", true)
}