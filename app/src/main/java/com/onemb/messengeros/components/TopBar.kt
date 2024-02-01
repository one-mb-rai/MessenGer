package com.onemb.messengeros.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import com.onemb.messengeros.messageList
import com.onemb.messengeros.model.SMSMessage

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(allMessages: SnapshotStateMap<String, List<SMSMessage>>, title: String, showAction: Boolean, navController: NavHostController) {
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
                            title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        if (showAction) {
                            IconButton(onClick = { /* do something */ }) {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = "Search Message",
                                )
                            }
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        messageList(allMessages, innerPadding, navController)
    }
}
