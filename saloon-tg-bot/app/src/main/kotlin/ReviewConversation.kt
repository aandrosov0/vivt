import com.github.kotlintelegrambot.dispatcher.handlers.MessageHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import models.ExposedReview
import models.Phone
import models.Service
import models.fromRussianName
import kotlin.random.Random

class ReviewConversation(
    chatId: Long,
    private val onEnd: (ExposedReview) -> Unit,
) : Conversation(chatId) {
    private lateinit var service: Service
    private var quality: Int? = null

    private lateinit var question1: String
    private lateinit var question2: String
    private lateinit var question3: String

    private lateinit var phone: Phone

    override fun MessageHandlerEnvironment.handleMessage() {
        when {
            !::service.isInitialized -> handleService()
            quality == null -> handleQuality()
            !::question1.isInitialized -> handleQuestion1()
            !::question2.isInitialized -> handleQuestion2()
            !::question3.isInitialized -> handleQuestion3()
            !::phone.isInitialized -> handlePhone()
            else -> handleEnd()
        }
    }

    private fun MessageHandlerEnvironment.handleService() {
        try {
            service = Service.fromRussianName("${message.text}")
            handleMessage()
        } catch (_: IllegalArgumentException) {
            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = "Выберите сервис",
                replyMarkup = Keyboards.buildServicesKeyboard(Service.entries)
            )
        }
    }

    private fun MessageHandlerEnvironment.handleQuality() {
        try {
            quality = "${message.text}".toInt()
            handleMessage()
        } catch (_: NumberFormatException) {
            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = "Оцените качество обслуживания от 1 до 5",
                replyMarkup = Keyboards.buildQualityKeyboard()
            )
        }
    }

    private fun MessageHandlerEnvironment.handleQuestion1() {
        val text = "${message.text}"
        if (text.length >= 12) {
            question1 = text
            copy(message = message.copy(text = "")).handleMessage()
        } else {
            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = when(quality) {
                    1 -> "Что именно вам не понравилось в услуге?(не менее 12 символов)"
                    2 -> "Какие аспекты услуги вам не понравились?(не менее 12 символов)"
                    3 -> "Что вам понравилось в услуге, а что нет?(не менее 12 символов)"
                    4 -> "Что вам особенно понравилось в услуге?(не менее 12 символов)"
                    5 -> "Что именно вам понравилось больше всего?(не менее 12 символов)"
                    else -> "Question 1 Error"
                },
                replyMarkup = KeyboardReplyMarkup.createSimpleKeyboard(listOf())
            )
        }
    }

    private fun MessageHandlerEnvironment.handleQuestion2() {
        val text = "${message.text}"
        if (text.length >= 12) {
            question2 = text
            copy(message = message.copy(text = "")).handleMessage()
        } else {
            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = when(quality) {
                    1 -> "Были ли какие-то конкретные моменты, которые вас разочаровали?(не менее 12 символов)"
                    2 -> "Как вы оцениваете баланс между качеством и ценой?(не менее 12 символов)"
                    3 -> "Что, по вашему мнению, можно было бы улучшить?(не менее 12 символов)"
                    4 -> "Есть ли какие-то мелочи, которые можно было бы улучшить?(не менее 12 символов)"
                    5 -> "Как вы оцениваете профессионализм и навыки мастера?(не менее 12 символов)"
                    else -> "Question 2 Error"
                }
            )
        }
    }

    private fun MessageHandlerEnvironment.handleQuestion3() {
        val text = "${message.text}"
        if (text.length >= 12) {
            question3 = text
            handleMessage()
        } else {
            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = when(quality) {
                    1 -> "Чувствовали ли вы себя комфортно во время процедуры?(не менее 12 символов)"
                    2 -> "Были ли у вас ожидания, которые не оправдались?(не менее 12 символов)"
                    3 -> "Как вы оцениваете общение с мастером?(не менее 12 символов)"
                    4 -> "Как вы оцениваете атмосферу в салоне?(не менее 12 символов)"
                    5 -> "Будете ли вы рекомендовать нас своим друзьям и почему?(не менее 12 символов)"
                    else -> "Question 3 Error"
                }
            )
        }
    }

    private fun MessageHandlerEnvironment.handlePhone() {
        message.contact?.run {
            phone = Phone(phoneNumber, firstName)
            handleMessage()
        } ?:
        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = "Оставьте свой номер телефона",
            replyMarkup = Keyboards.buildContactKeyboard()
        )
    }

    private fun MessageHandlerEnvironment.handleEnd() {
        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = if (quality!! >= 1 && quality!! <= 3) {
                """
                    Извините, что ваши ожидания не оправдались. 
                    Мы будем стараться улучшить наш сервис. 
                    Дарим вам скидку на 20% по промокоду SKIDKA${Random.nextInt()}
                """.trimIndent()
            } else {
                """
                    Спасибо за отзыв!
                """.trimIndent()
            }
        )

        onEnd(
            ExposedReview(
                service = service,
                quality = quality!!,
                phone = phone
            )
        )
    }
}
