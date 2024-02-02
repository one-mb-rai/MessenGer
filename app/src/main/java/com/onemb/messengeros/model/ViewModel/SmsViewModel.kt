import android.app.Application
import android.content.ContentResolver
import android.provider.Telephony
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.onemb.messengeros.model.SMSMessage
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
        val contentResolver: ContentResolver = getApplication<Application>().contentResolver
        val cursor = contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            null,
            null,
            null,
            "date DESC"
        )

        val smsMap = mutableMapOf<String, MutableList<SMSMessage>>()

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

        return smsMap
    }

    fun filterSmsList(sender: String): Map<String, List<SMSMessage>> {
        return _smsList.value.filterKeys { it.contains(sender, ignoreCase = true) }
            .mapValues { entry ->
                entry.value.sortedBy { it.date }
            }
    }
}
