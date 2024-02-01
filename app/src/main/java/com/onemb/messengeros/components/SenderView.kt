import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
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
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            SenderHeader(sender = sender, latestMessage = latestMessage.value)
        }

        item {
            LatestMessage(message = latestMessage.value?.message)
        }
    }
}

@Composable
fun SenderHeader(sender: String, latestMessage: SMSMessage?) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = sender,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = latestMessage?.date?.parsedDate()?.split(" ")?.get(1) ?: "",
            color = Color.Gray
        )
    }
}

@Composable
fun LatestMessage(message: String?) {
    if (message != null) {
        Text(
            text = message,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
