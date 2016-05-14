package chonx.bot

import chonx.bot.telegram.Telegram
import chonx.bot.telegram.types.Update

abstract class Bot {
  abstract fun onUpdate(update: Update)
}
