package chonx.statemachine

import chonx.core.Die
import chonx.core.Game
import chonx.core.IllegalMoveException
import chonx.core.NotYourTurnException
import chonx.core.Player
import chonx.core.PreGame
import chonx.core.RandomDie
import chonx.statemachine.Command.AddPlayer
import chonx.statemachine.Command.BeginGame
import chonx.statemachine.Phase.CollectPlayers

class StateMachine(val phase: Phase, val die: Die) {
  companion object {
    fun new(die: Die = RandomDie()) = StateMachine(CollectPlayers(PreGame.Companion.new()), die)

    private inline fun <reified P : Phase, reified C : Command> reducer(noinline reduceFunc: (P, C) -> Phase): Reducer<P, C> {
      return Reducer(P::class.java, C::class.java, reduceFunc)
    }
  }

  private val reducers = listOf(

      reducer<CollectPlayers, AddPlayer> { phase, command ->
        CollectPlayers(phase.preGame.addPlayer(command.player))
      },

      reducer<CollectPlayers, BeginGame> { phase, command ->
        Phase.InGame(Game.new(phase.preGame.players()))
      },

      reducer<Phase.InGame, Command.RollDice> { phase, command ->
        Phase.InMove(phase.game, phase.game.roll(die))
      },

      reducer<Phase.InMove, Command.LockDice> { phase, command ->
        Phase.InMove(phase.game,
            command.indices.fold(phase.moveInProgress, { m, index -> m.lock(index) }))
      },

      reducer<Phase.InMove, Command.UnlockDice> { phase, command ->
        Phase.InMove(phase.game,
            command.indices.fold(phase.moveInProgress, { m, index -> m.unlock(index) }))
      },

      reducer<Phase.InMove, Command.RollDice> { phase, command ->
        if (phase.moveInProgress.rollsLeft <= 0) {
          throw IllegalMoveException("MoveInProgress has no rolls left")
        } else if (phase.moveInProgress.rollsLeft > 1) {
          Phase.InMove(phase.game, phase.moveInProgress.roll())
        } else {
          Phase.PickSlot(phase.game, phase.moveInProgress.roll())
        }
      },

      reducer<Phase.InMove, Command.AcceptDice> { phase, command ->
        Phase.PickSlot(phase.game, phase.moveInProgress)
      },

      reducer<Phase.PickSlot, Command.PickSlot> { phase, command ->
        val nextGame = phase.game.move(phase.moveInProgress, command.slot)
        if (nextGame.hasEnded()) {
          Phase.Ended(nextGame)
        } else {
          Phase.InGame(nextGame)
        }
      }

  ).map { reducer ->
    Pair(reducer.phaseClass, reducer.commandClass) to reducer
  }.toMap()

  fun handle(player: Player?, command: Command): StateMachine {
    if (phase is Phase.GamePhase && player != phase.game.currentPlayer) {
      throw NotYourTurnException()
    }

    return StateMachine(reduce(phase, command), die)
  }

  @Suppress("UNCHECKED_CAST")
  fun <P : Phase> phase(): P = phase as P

  private fun reduce(phase: Phase, command: Command): Phase {
    reducers.get(phase.javaClass to command.javaClass)?.let {
      return it.reduce(phase, command)
    }

    throw IllegalMoveException("${phase} + ${command} can't be handled")
  }
}
