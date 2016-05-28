package chonx.bot

import chonx.bot.telegram.Telegram
import chonx.bot.telegram.types.KeyboardButton
import chonx.bot.telegram.types.ReplyKeyboardMarkup
import chonx.bot.telegram.types.Update

class EchoBot(private val telegram: Telegram, private val runner: BotRunner) : Bot() {
  fun start() {
    runner.updates().subscribe {
      onUpdate(it)
    }
  }

  override fun onUpdate(update: Update) {
    update.message?.let { msg ->
      val keyboard = ReplyKeyboardMarkup(listOf(
          listOf(KeyboardButton("Cool!")),
          listOf(KeyboardButton("Nice"), KeyboardButton("Not Nice"))
      ), one_time_keyboard = true, selective = true)
      telegram.sendMessage(msg.chat.id, "Hey @${msg.from?.username}, you said ${msg.text}", keyboard)
    }
  }
}
