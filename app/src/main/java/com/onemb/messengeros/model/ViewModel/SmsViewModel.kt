import android.app.Application
import android.content.ContentResolver
import android.provider.Telephony
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.onemb.messengeros.model.SMSMessage
import com.onemb.messengeros.model.ConversationArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SmsViewModel(application: Application) : AndroidViewModel(application) {

    private val _smsList = MutableStateFlow<Map<String, List<SMSMessage>>>(emptyMap())
    val smsList: StateFlow<Map<String, List<SMSMessage>>> get() = _smsList

    init {
        loadSms()
    }

    private fun loadSms() {
        viewModelScope.launch(Dispatchers.IO) {
            val smsMap = fetchSms()
            _smsList.value = smsMap
        }
    }

    private fun fetchSms(): Map<String, List<SMSMessage>> {
        val smsInboxUri = Telephony.Sms.Inbox.CONTENT_URI
        val smsSentUri = Telephony.Sms.Sent.CONTENT_URI
        val contentResolver: ContentResolver = getApplication<Application>().contentResolver

        val smsCursor = contentResolver.query(
            smsInboxUri,
            null,
            null,
            null,
            "date DESC"
        )

        val sentCursor = contentResolver.query(
            smsSentUri,
            null,
            null,
            null,
            "date DESC"
        )

        val smsMap = mutableMapOf<String, MutableList<SMSMessage>>()
        fun processCursor(cursor: android.database.Cursor?) {
            cursor?.use {
                val indexMessage = it.getColumnIndex("body")
                val indexSender = it.getColumnIndex("address")
                val indexDate = it.getColumnIndex("date")
                val indexRead = it.getColumnIndex("read")
                val indexType = it.getColumnIndex("type")
                val indexThread = it.getColumnIndex("thread_id")
                val indexService = it.getColumnIndex("service_center")

                while (it.moveToNext()) {
                    val smsMessage = SMSMessage(
                        message = it.getString(indexMessage),
                        sender = it.getString(indexSender),
                        date = it.getLong(indexDate),
                        read = it.getString(indexRead).toBoolean(),
                        type = it.getInt(indexType),
                        thread = it.getInt(indexThread),
                        service = it.getString(indexService) ?: ""
                    )

                    // Group SMS messages by sender
                    val senderKey = it.getString(indexSender)
                    if (senderKey != null) {
                        if (smsMap.containsKey(senderKey)) {
                            smsMap[senderKey]?.add(smsMessage)
                        } else {
                            smsMap[senderKey] = mutableListOf(smsMessage)
                        }
                    }
                }
            }
        }

        processCursor(smsCursor)
        processCursor(sentCursor)
        smsMap.forEach { (_, messages) ->
            messages.sortByDescending { it.date }
        }
        val sortedSmsMap = smsMap.toList().sortedByDescending { (_, messages) ->
            messages.firstOrNull()?.date ?: 0
        }.toMap()

        return sortedSmsMap
    }

    fun filterSmsList(sender: ConversationArgs): Map<String, List<SMSMessage>> {
        val filterVal = sender.senderName
        return if(filterVal != null) {
            _smsList.value.filterKeys { it.contains(filterVal, ignoreCase = true) }
                .mapValues { entry ->
                    entry.value.sortedBy { it.date }
                }
        } else {
            _smsList.value
        }
    }
}
