package chonx.cli

import chonx.bot.telegram.types.ReplyKeyboardMarkup
import chonx.core.Player

interface MessageSink {
  fun send(player: Player, msg: String, keyboard: ReplyKeyboardMarkup? = null)
}
