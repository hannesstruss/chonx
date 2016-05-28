package chonx.cli

import chonx.core.IllegalMoveException
import chonx.core.NotEnoughPlayersException
import chonx.core.Player
import chonx.core.SlotAlreadyFilledException
import chonx.statemachine.Command
import chonx.statemachine.Phase
import chonx.statemachine.StateMachine
import rx.Observable

class GameLoop(private val commands: Observable<Command>,
               private val send: (Player, String) -> Unit) {
  private var state = StateMachine.new()

  fun start() {
    status()
    commands.subscribe {
      try {
        state = state.handle(it)
      } catch (e: NotEnoughPlayersException) {
        println("Not enough players")
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
      send(Player("Hannes"), """Players: ${phase.preGame.players.map { it.name }.joinToString(", ")}""")
    } else if (phase is Phase.GamePhase) {
      send(Player("Hannes"), "${phase.javaClass.simpleName} - Current: ${phase.game.currentPlayer.name} (${phase.game.score(phase.game.currentPlayer)})")

      if (phase is Phase.MovePhase) {
        val msg = phase.moveInProgress.dice()
            .mapIndexed { index, die ->
              if (phase.moveInProgress.isLocked(index)) {
                "(${die})"
              } else {
                die.toString()
              }
            }.joinToString(" ")
        send(Player("Hannes"), msg)
      }
    } else if (phase is Phase.Ended) {
      send(Player("Hannes"), "Done!")
    }
  }
}
