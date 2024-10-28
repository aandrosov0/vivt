import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.handlers.MessageHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.entities.ChatId
import daos.DAO
import kotlinx.coroutines.Runnable
import models.Conversation
import models.ExposedReview
import models.ReviewConversation
import models.russianName

class Bot(
    private val token: String,
    private val reviewDAO: DAO<ExposedReview>
) : Runnable {
    private val conversations = mutableListOf<Conversation>()

    override fun run() {
        bot {
            this.token = this@Bot.token
            dispatch {
                message { handleMessage() }
            }
        }.startPolling()
    }

    private fun MessageHandlerEnvironment.handleMessage() {
        val chatId = message.chat.id

        val conversation = conversations.find { it.chatId == chatId }
        if (conversation != null) {
            conversation.onMessage(this)
            return
        }

        when (message.text) {
            "Посмотреть отзывы" -> handleLookReviewsCommand()
            "Оставить отзыв" -> handleLeaveReviewCommand()
            else -> bot.sendMessage(
                ChatId.fromId(chatId),
                "Выберите опцию",
                replyMarkup = Keyboards.buildDefaultOptionsKeyboard()
            )
        }
    }

    private fun MessageHandlerEnvironment.handleLookReviewsCommand() {
        reviewDAO.readAll().forEach {
            bot.sendMessage(
                chatId = ChatId.fromId(message.chat.id),
                text = """
                    Отзыв от ${it.reviewer} (${it.phone})
                    Услуга: ${it.service.russianName}
                    Качество: ${it.quality}
                """.trimIndent()
            )
        }
    }

    private fun MessageHandlerEnvironment.handleLeaveReviewCommand() {
        val review = ReviewConversation(message.chat.id, ::registerReview)
        conversations.add(review)
        review.onMessage(this)
    }

    private fun registerReview(chatId: Long, review: ExposedReview) {
        conversations.removeIf { it.chatId == chatId }
        reviewDAO.create(review)
    }
}