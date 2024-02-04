package com.onemb.messengeros.pages

import SenderView
import SmsViewModel
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.onemb.messengeros.model.SMSMessage
import com.onemb.messengeros.navigation.Screen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onemb.messengeros.R
import com.onemb.messengeros.model.ConversationArgs
import com.onemb.messengeros.model.ViewModel.AppBarViewModel
import kotlinx.coroutines.flow.StateFlow


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesListScreen(navController: NavHostController, viewModel: SmsViewModel = viewModel()) {
    val darkTheme: Boolean = isSystemInDarkTheme()
    val appBarViewModel: AppBarViewModel = viewModel()
    val allMessages = viewModel.smsList
    appBarViewModel.mainScreenAppBarChange(darkTheme, LocalView.current)

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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher),
                                contentDescription = stringResource(id = R.string.app_name),
                                modifier = Modifier
                                    .width(35.dp)
                                    .height(35.dp)
                            )
                            Text(
                                "Messen Ger",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(start = 20.dp)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.Search.route) }) {
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


@SuppressLint("StateFlowValueCalledInComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun messageList(allMessages: StateFlow<Map<String, List<SMSMessage>>>, innerPadding: PaddingValues, navController: NavHostController) {
    Scaffold(
        modifier = Modifier.padding(innerPadding),
        floatingActionButton = {
            StartChat(navController)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 5.dp,
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
                itemContent = { index ->
                    val dataIndex = allMessages.value.toList()[index].first
                    val dataValue = allMessages.value.toList()[index].second
                    SenderListItem(sender = dataIndex, messages = dataValue, navController)
                }
            )
        }
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

@Composable
fun StartChat(navController: NavHostController) {
    ExtendedFloatingActionButton(
        onClick = { navController.navigate(Screen.NewConversation.route) },
        icon = { Icon(Icons.Filled.Email, "Start chat button") },
        text = { Text(text = "Start Chat") },
    )
}