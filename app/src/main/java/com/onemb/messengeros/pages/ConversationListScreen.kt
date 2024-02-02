package com.onemb.messengeros.pages

import SmsViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.onemb.messengeros.components.MessageView
import com.onemb.messengeros.model.SMSMessage
import com.onemb.messengeros.model.parsedDate
import com.onemb.messengeros.model.readMessages

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationListScreen(args: NavHostController, senderName: String?, viewModel: SmsViewModel = viewModel()) {

    val listState = rememberLazyListState()
    val filteredSmsList = senderName?.let { viewModel.filterSmsList(it) }


    LaunchedEffect(key1 = filteredSmsList) {
        if (filteredSmsList?.isNotEmpty() == true) {
            listState.scrollToItem(Int.MAX_VALUE)
        }
    }
    val darkTheme: Boolean = isSystemInDarkTheme()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if(darkTheme) Color(0xFF2B201D) else Color(0xFFF9F2F5),
                    titleContentColor = if(darkTheme) Color(0xFFF9F2F5) else Color(0xFF2B201D),
                ),
                title = {
                    if (senderName != null) {
                        Text(
                            senderName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        args.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back Button",
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(innerPadding),
            state = listState
        ) {
            filteredSmsList?.forEach { (_, messages) ->
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
}