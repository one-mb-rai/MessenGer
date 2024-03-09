package com.onemb.messengeros.pages

import SmsViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.onemb.messengeros.model.SMSMessage
import com.onemb.messengeros.model.ViewModel.AppBarViewModel

@Composable
fun SearchScreen(navController: NavHostController) {
    val appBarModel: AppBarViewModel = viewModel()
    val smsModel: SmsViewModel = viewModel()

    var text by remember { mutableStateOf("") }
    smsModel.searchSms(text)
    val keyboardController = LocalSoftwareKeyboardController.current
    val darkTheme: Boolean = isSystemInDarkTheme()
    appBarModel.searchScreenAppBarChange(darkTheme, LocalView.current)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.90f),
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 10.dp,
                            color = Color(0xFFf4e9ea),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .background(color = Color(0xFFf4e9ea), shape = RoundedCornerShape(30.dp))
                        .height(56.dp),
                    placeholder = {
                        Text(text = "Search messages")
                    },
                    leadingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .clickable {
                                    navController.popBackStack()
                                }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null,
                                tint = LocalContentColor.current
                            )
                        }
                    },
                    trailingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .clickable {
                                    keyboardController?.hide()
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = null,
                                tint = LocalContentColor.current
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                    shape = RoundedCornerShape(30.dp)
                )
            }
            if (text.length > 3) {
                SmsList(smsList = smsModel.searchedSmsList.value, navController)
            }
        }
    }
}

@Composable
fun SmsList(smsList: Map<String, List<SMSMessage>>, navController: NavHostController) {
    Column (
        modifier = Modifier.fillMaxWidth(0.90f)
            .padding(top = 16.dp),
    ){
        smsList.entries.forEach { (sender, messages) ->
            SenderListItem(
                sender = sender,
                messages = messages,
                navController
            )
        }
    }
}
