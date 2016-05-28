package chonx.cli;

import chonx.bot.BotRunner
import chonx.bot.telegram.Telegram
import chonx.bot.telegram.types.Message
import chonx.bot.telegram.types.ReplyKeyboardMarkup
import chonx.bot.telegram.types.User
import chonx.core.Player
import chonx.statemachine.Command

class ChonxBot(val telegram: Telegram, val runner: BotRunner) {
  private val loops: MutableMap<Int, GameLoop> = mutableMapOf()
  private val updates = runner.updates().share()

  private val slashRegex = """^(/)""".toRegex()
  private val nameRegex = """(@ChonxBot)""".toRegex()

  fun start() {
    updates.subscribe { update ->
      update.message?.let { message ->
        println(message)
        val chatId = message.chat.id
        val loop = loops.getOrPut(chatId, { createLoop(chatId) })

        if (message.text != null && message.from != null) {
          val player = player(message.from!!)
          parseSilent(sanitize(message), player)?.let {
            loop.handle(player(message.from!!), it)
          }
        }
      }
    }
  }

  private fun createLoop(chatId: Int): GameLoop {
    return GameLoop(TelegramMessageSink(telegram, chatId))
  }

  private fun player(user: User) = Player(user.first_name, user.username ?: user.first_name)

  private fun sanitize(message: Message): String {
    var msg = message.text.toString()
    listOf(slashRegex, nameRegex).forEach {
      msg = it.replace(msg, "")
    }
    return msg
  }

  private fun parseSilent(input: String, player: Player): Command? {
    try {
      return parse(input, player)
    } catch (e: ParseException) {
      println("Can't parse ${input}")
      return null
    }
  }
}

class TelegramMessageSink(private val telegram: Telegram,
                          private val chatId: Int) : MessageSink {
  override fun send(player: Player, msg: String, keyboard: ReplyKeyboardMarkup?) {
    telegram.sendMessage(chatId, "@${player.username}: ${msg}", keyboard)
  }
}
