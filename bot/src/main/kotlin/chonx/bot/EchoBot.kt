package chonx.bot

import chonx.bot.telegram.Telegram
import chonx.bot.telegram.types.Update

class EchoBot(private val telegram: Telegram) : Bot() {
  override fun onUpdate(update: Update) {
    update.message?.let { msg ->
      telegram.sendMessage(msg.chat.id, "You said ${msg.text}")
    }
  }
}
