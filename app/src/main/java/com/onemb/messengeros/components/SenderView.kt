import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.onemb.messengeros.model.SMSMessage
import com.onemb.messengeros.model.parsedDate

@Composable
fun SenderView(sender: String, messages: List<SMSMessage>) {
    val latestMessage = rememberUpdatedState(newValue = messages.maxByOrNull { it.date })

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.18f),
                ) {
                    OutlinedIconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.AccountBox,
                            contentDescription = "Person Icon",
                        )
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    SenderHeader(sender = sender, latestMessage = latestMessage.value)
                    LatestMessage(message = latestMessage.value?.message)
                }
            }
        }

    }
}

@Composable
fun SenderHeader(sender: String, latestMessage: SMSMessage?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
            Text(
                text = sender,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = latestMessage?.date?.parsedDate() ?: "",
                color = Color.Gray,
                overflow = TextOverflow.Ellipsis
            )
    }
}

@Composable
fun LatestMessage(message: String?) {
    if (message != null) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = message,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
