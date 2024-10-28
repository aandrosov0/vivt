package models

import Keyboards
import com.github.kotlintelegrambot.dispatcher.handlers.MessageHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import kotlin.random.Random

class ReviewConversation(
    chatId: Long,
    private val onEndConversation: (Long, ExposedReview) -> Unit,
) : Conversation(chatId) {
    private var quality: Int? = null
    private lateinit var service: Service
    private lateinit var phoneNumber: String
    private lateinit var name: String

    private fun MessageHandlerEnvironment.endHandling() {
        val promoText = when (quality) {
            in 2..3 -> """
                    Спасибо за отзыв, $name ($phoneNumber)
                    Высылаем вам промо-код на 20 процентов: 0X${Random.nextInt(100, 1000)}
                """.trimIndent()
            1 -> """
                    Спасибо за отзыв, $name ($phoneNumber)
                    Высылаем вам промо-код на 50 процентов: 0X${Random.nextInt(100, 1000)}
                """.trimIndent()
            else -> """
                    Спасибо за отзыв, $name ($phoneNumber)
                    Ждем вас в следующий раз!
                """.trimIndent()
        }

        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = promoText,
            replyMarkup = Keyboards.buildDefaultOptionsKeyboard()
        )

        onEndConversation(
            chatId,
            ExposedReview(
                service = service,
                quality = quality!!,
                phone = phoneNumber,
                reviewer = name
            )
        )
    }

    private fun MessageHandlerEnvironment.handlePhoneNumber() {
        val contact = message.contact
        if (contact == null) {
            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = "Отправьте свой номер телефона",
                replyMarkup = Keyboards.buildContactKeyboard()
            )
            return
        }
        phoneNumber = contact.phoneNumber
        name = contact.firstName
        endHandling()
    }

    private fun MessageHandlerEnvironment.handleService() {
        try {
            service = serviceFromRussianName(message.text ?: "")
            handlePhoneNumber()
        } catch (exception: IllegalArgumentException) {
            bot.sendMessage(
                    chatId = ChatId.fromId(chatId),
                    text = "Какой услугой вы воспользовались?",
                    replyMarkup = Keyboards.buildServicesKeyboard(Service.entries)
            )
        }
    }

    private fun MessageHandlerEnvironment.handleQuality() {
        val text = message.text ?: ""
        val isMatching = text.matches(Regex("^[1-5]$"))

        if (isMatching) {
            quality = text.toInt()
            handleService()
            return
        }

        bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = "Поставьте оценку от 1 до 5",
                replyMarkup = Keyboards.buildQualityKeyboard()
        )
    }

    override fun MessageHandlerEnvironment.handleMessage() {
        when {
            quality == null -> handleQuality()
            !::service.isInitialized -> handleService()
            !::phoneNumber.isInitialized -> handlePhoneNumber()
        }
    }
}
