package chonx.bot

import chonx.bot.telegram.Telegram

class BotRunner(private val telegram: Telegram) {
  private val updates = Updates(telegram)

  fun updates() = updates.updates()
}
