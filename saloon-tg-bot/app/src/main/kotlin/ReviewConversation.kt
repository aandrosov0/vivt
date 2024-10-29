import com.github.kotlintelegrambot.dispatcher.handlers.MessageHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import models.Service

class ReviewConversation(
    chatId: Long,
) : Conversation(chatId) {
    private var currentQuestion = question(ReviewQuestion.SERVICE) {
        text = "Выберите услугу"
        expectations = listOf("Макияж", "Парикмахерская", "Боди-арт")

        question(ReviewQuestion.QUALITY) {
            text = "Оцените качество обслуживания"
            expectations = listOf("1", "2", "3", "4", "5")

            question(ReviewQuestion.QUESTION_1) {
                text = "Что именно вам не понравилось в услуге?"

                question(ReviewQuestion.QUESTION_2) {
                    text = "Были ли какие-то конкретные моменты, которые вас разочаровали?"

                    question(ReviewQuestion.QUESTION_3) {
                        text = "Чувствовали ли вы себя комфортно во время процедуры?"
                    }
                }
            }

            question(ReviewQuestion.QUESTION_1) {
                text = "Какие аспекты услуги вам не понравились?"

                question(ReviewQuestion.QUESTION_2) {
                    text = "Что, по вашему мнению, можно было бы улучшить?"

                    question(ReviewQuestion.QUESTION_3) {
                        text = "Были ли у вас ожидания, которые не оправдались?"
                    }
                }
            }

            question(ReviewQuestion.QUESTION_1) {
                text = "Что вам понравилось в услуге, а что нет?"

                question(ReviewQuestion.QUESTION_2) {
                    text = "Как вы оцениваете баланс между качеством и ценой?"

                    question(ReviewQuestion.QUESTION_3) {
                        text = "Как вы оцениваете общение с мастером?"
                    }
                }
            }

            question(ReviewQuestion.QUESTION_1) {
                text = "Что вам особенно понравилось в услуге?"

                question(ReviewQuestion.QUESTION_2) {
                    text = "Есть ли какие-то мелочи, которые можно было бы улучшить?"

                    question(ReviewQuestion.QUESTION_3) {
                        text = "Как вы оцениваете атмосферу в салоне?"
                    }
                }
            }

            question(ReviewQuestion.QUESTION_1) {
                text = "Что именно вам понравилось больше всего?"

                question(ReviewQuestion.QUESTION_2) {
                    text = "Как вы оцениваете профессионализм и навыки мастера?"

                    question(ReviewQuestion.QUESTION_3) {
                        text = "Будете ли вы рекомендовать нас своим друзьям и почему?"
                    }
                }
            }
        }
    }

    private lateinit var service: Service

    override fun MessageHandlerEnvironment.handleMessage() {
        when (currentQuestion.id) {
            ReviewQuestion.SERVICE -> handleService()
        }
    }

    private fun MessageHandlerEnvironment.handleService() {
        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = currentQuestion.question,
            replyMarkup = Keyboards.generateKeyboard(currentQuestion.expectations)
        )
    }
}
