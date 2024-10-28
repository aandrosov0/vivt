import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup

interface ResponseReceiver {
    fun onMessage(chatId: Long, text: String, replyMarkup: KeyboardReplyMarkup? = null)
}