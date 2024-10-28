package models

import com.github.kotlintelegrambot.dispatcher.handlers.MessageHandlerEnvironment

abstract class Conversation(
    val chatId: Long,
) {
    fun onMessage(messageHandlerEnvironment: MessageHandlerEnvironment) {
        messageHandlerEnvironment.handleMessage()
    }

    protected abstract fun MessageHandlerEnvironment.handleMessage()
}
