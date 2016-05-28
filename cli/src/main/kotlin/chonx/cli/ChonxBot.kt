package chonx.cli;

import chonx.bot.BotRunner
import chonx.bot.telegram.Telegram
import chonx.bot.telegram.types.Message
import chonx.bot.telegram.types.User
import chonx.core.Player
import chonx.statemachine.Command
import rx.Observable

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
        if (chatId !in loops) {
          val loop = GameLoop(
              commands(chatId),
              { player, msg ->
                telegram.sendMessage(chatId, msg)
              }
          )
          loops.put(chatId, loop)
          loop.start()
        }
      }
    }
  }

  private fun commands(chatId: Int): Observable<Pair<Player, Command>> {
    return updates
        .map { it.message }
        .filterNotNull()
        .doOnNext { println(it.text) }
        .filter { it.chat.id == chatId }
        .map { message ->
          if (message.text != null && message.from != null) {
            val player = player(message.from!!)
            parseSilent(sanitize(message), player.name)?.let {
              Pair(player, it)
            }
          } else {
            null
          }
        }
        .filterNotNull()
  }

  private fun player(user: User) = Player(user.first_name)

  private fun sanitize(message: Message): String {
    var msg = message.text.toString()
    listOf(slashRegex, nameRegex).forEach {
      msg = it.replace(msg, "")
    }
    return msg
  }

  private fun parseSilent(input: String, player: String): Command? {
    try {
      return parse(input, player)
    } catch (e: ParseException) {
      println("Can't parse ${input}")
      return null
    }
  }

  @Suppress("CAST_NEVER_SUCCEEDS")
  fun <T : Any> Observable<T?>.filterNotNull() = this.filter { it != null } as Observable<T>
}
