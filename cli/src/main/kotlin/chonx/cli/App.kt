package chonx.cli

import chonx.bot.BotRunner
import chonx.bot.telegram.Telegram
import chonx.core.Player
import chonx.statemachine.Command
import rx.Observable

fun main(args: Array<String>) {
  val telegram = Telegram.fromToken(System.getProperty("chonx.bot"))
  val runner = BotRunner(telegram)
  val bot = ChonxBot(telegram, runner)
  bot.start()
}

interface InputAdapter {
  fun commands(): Observable<Pair<Player, Command>>
  fun send(player: Player, msg: String)
}

//class CliInputAdapter : InputAdapter {
//  private val commands: PublishSubject<Pair<Player, Command>> = PublishSubject.create()
//
//  override fun commands() = commands
//
//  override fun send(player: Player, msg: String) {
//    println("@${player.name}: ${msg}")
//  }
//
//  fun start() {
//    while (true) {
//      print("> ")
//      val input = readLine()
//
//      if (input == null) {
//        break
//      }
//
//      try {
//        val cmd = parse(input.trim(), "Hannes")
//        commands.onNext(Player("Hannes") to cmd)
//      } catch (e: ParseException) {
//        println(e.message)
//        continue
//      }
//    }
//  }
//}

