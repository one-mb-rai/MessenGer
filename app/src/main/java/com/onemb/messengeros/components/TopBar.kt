package com.onemb.messengeros.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.onemb.messengeros.messageList
import com.onemb.messengeros.model.SMSMessage

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(allMessages: SnapshotStateMap<String, List<SMSMessage>>, title: String, showAction: Boolean, navController: NavHostController) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .drawBehind {
                        // Add a border-bottom to the TopAppBar
                        drawLine(
                            brush = Brush.horizontalGradient(listOf(Color.Gray, Color.Transparent)),
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1f
                        )
                    }
            ) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent, // Set the container color to transparent
                        titleContentColor = Color.Black,
                    ),
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = MaterialTheme.shapes.small
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
