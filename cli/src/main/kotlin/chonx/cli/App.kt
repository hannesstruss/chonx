package chonx.cli

import chonx.core.IllegalMoveException
import chonx.core.Player
import chonx.core.SlotAlreadyFilledException
import chonx.statemachine.Command
import chonx.statemachine.Phase
import chonx.statemachine.StateMachine
import rx.Observable
import rx.subjects.PublishSubject

fun main(args: Array<String>) {
  val inputAdapter = CliInputAdapter()
  val gameLoop = GameLoop(inputAdapter)
  gameLoop.start()
  inputAdapter.start()
}

interface InputAdapter {
  fun commands(): Observable<Pair<Player, Command>>
  fun send(player: Player, msg: String)
}

class CliInputAdapter : InputAdapter {
  private val commands: PublishSubject<Pair<Player, Command>> = PublishSubject.create()

  override fun commands() = commands

  override fun send(player: Player, msg: String) {
    println("@${player.name}: ${msg}")
  }

  fun start() {
    while (true) {
      print("> ")
      val input = readLine()

      if (input == null) {
        break
      }

      try {
        val cmd = parse(input.trim())
        commands.onNext(Player("Hannes") to cmd)
      } catch (e: ParseException) {
        println(e.message)
        continue
      }
    }
  }
}

class GameLoop(val adapter: InputAdapter) {
  private var state = StateMachine.new()

  fun start() {
    status()
    adapter.commands().subscribe {
      try {
        state = state.handle(it.second)
      } catch (e: IllegalMoveException) {
        println(e.message)
      } catch (e: SlotAlreadyFilledException) {
        println(e.message)
      }
      status()
    }
  }

  private fun status() {
    val phase = state.phase
    if (phase is Phase.CollectPlayers) {
      adapter.send(Player("Hannes"), """Players: ${phase.preGame.players.map { it.name }.joinToString(", ")}""")
    } else if (phase is Phase.GamePhase) {
      adapter.send(Player("Hannes"), "${phase.javaClass.simpleName} - Current: ${phase.game.currentPlayer.name} (${phase.game.score(phase.game.currentPlayer)})")

      if (phase is Phase.MovePhase) {
        val msg = phase.moveInProgress.dice()
            .mapIndexed { index, die ->
              if (phase.moveInProgress.isLocked(index)) {
                "(${die})"
              } else {
                die.toString()
              }
            }.joinToString(" ")
        adapter.send(Player("Hannes"), msg)
      }
    } else if (phase is Phase.Ended) {
      adapter.send(Player("Hannes"), "Done!")
      System.exit(0)
    }
  }
}

