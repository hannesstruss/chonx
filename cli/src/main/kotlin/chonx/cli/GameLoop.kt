package chonx.cli

import chonx.bot.telegram.keyboard
import chonx.bot.telegram.types.ReplyKeyboardMarkup
import chonx.core.IllegalMoveException
import chonx.core.NotEnoughPlayersException
import chonx.core.NotYourTurnException
import chonx.core.Player
import chonx.core.SlotAlreadyFilledException
import chonx.statemachine.Command
import chonx.statemachine.Phase
import chonx.statemachine.StateMachine

class GameLoop(private val sink: MessageSink) {
  private var state = StateMachine.new()

  fun start() {
  }

  fun handle(player: Player, command: Command) {
    try {
      state = state.handle(player, command)
      status(player)
    } catch (e: NotYourTurnException) {
      println("Ignoring command, not your turn")
    } catch (e: NotEnoughPlayersException) {
      println("Not enough players")
    } catch (e: IllegalMoveException) {
      println(e.message)
    } catch (e: SlotAlreadyFilledException) {
      println(e.message)
    }
  }

  private fun status(player: Player) {
    val phase = state.phase
    if (phase is Phase.CollectPlayers) {
      sink.send(player, """Players: ${phase.preGame.players.map { it.name }.joinToString(", ")}""")
    } else if (phase is Phase.GamePhase) {
      sink.send(player, "${phase.javaClass.simpleName} - Current: ${phase.game.currentPlayer.name} (${phase.game.score(phase.game.currentPlayer)})")

      if (phase is Phase.MovePhase) {
        val kb = keyboard {
          one_time_keyboard = true
          selective = true

          row {
            +"roll"
            +"accept"
          }
          row {
            (1..5).forEach { +"lock ${it}" }
          }
        }

        val msg = phase.moveInProgress.dice()
            .mapIndexed { index, die ->
              if (phase.moveInProgress.isLocked(index)) {
                "(${die})"
              } else {
                die.toString()
              }
            }.joinToString(" ")
        sink.send(player, msg, kb)
      }
    } else if (phase is Phase.Ended) {
      sink.send(player, "Done!")
    }
  }
}
