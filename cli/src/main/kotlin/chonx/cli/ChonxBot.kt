package chonx.cli;

import chonx.bot.BotRunner
import chonx.bot.telegram.Telegram
import chonx.core.Player
import chonx.statemachine.Command
import rx.Observable

class ChonxBot(val telegram: Telegram, val runner: BotRunner) {
  private val loops: MutableMap<Int, GameLoop> = mutableMapOf()
  private val updates = runner.updates().share()

  private val slashRegex = """^(/)""".toRegex()
  private val nameRegex = """(@ChonxBot)""".toRegex()

  private fun commands(chatId: Int): Observable<Command> {
    return updates
        .map { it.message }
        .filterNotNull()
        .doOnNext { println(it.text) }
        .filter { it.chat.id == chatId }
        .map { message ->
          message.text?.let {
            val player = Player(message.from!!.first_name)

            val input = nameRegex.replace(slashRegex.replace(it, ""), "")
            try {
              parse(input, player)
            } catch (e: ParseException) {
              println("Can't parse ${input}")
              null
            }
          }
        }
        .filterNotNull()
  }

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

  @Suppress("CAST_NEVER_SUCCEEDS")
  fun <T> Observable<T?>.filterNotNull() = this.filter { it != null } as Observable<T>
}
