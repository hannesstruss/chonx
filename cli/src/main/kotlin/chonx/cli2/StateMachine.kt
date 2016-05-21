package chonx.cli2

import chonx.cli2.Command.AddPlayer
import chonx.cli2.Command.BeginGame
import chonx.cli2.Phase.CollectPlayers
import chonx.core.Die
import chonx.core.Game
import chonx.core.PreGame
import chonx.core.RandomDie

class Reducer<P : Phase, C : Command>(
    val phaseClass: Class<P>,
    val commandClass: Class<C>,
    val reduceFunc: (P, C) -> Phase
) {
  @Suppress("UNCHECKED_CAST")
  fun reduce(phase: Phase, command: Command): Phase {
    return reduceFunc(phase as P, command as C)
  }
}

private inline fun <reified P : Phase, reified C : Command> reducer(noinline reduceFunc: (P, C) -> Phase): Reducer<P, C> {
  return Reducer(P::class.java, C::class.java, reduceFunc)
}


@Suppress("UNCHECKED_CAST")
class StateMachine(val phase: Phase, val die: Die) {
  companion object {
    fun new(die: Die = RandomDie()) = StateMachine(CollectPlayers(PreGame.new()), die)
  }

  private val reducers = listOf(

      reducer<CollectPlayers, AddPlayer> { phase, command ->
        CollectPlayers(phase.preGame.addPlayer(command.name))
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
          throw IllegalStateException("MoveInProgress has no rolls left")
        } else if (phase.moveInProgress.rollsLeft > 1) {
          Phase.InMove(phase.game, phase.moveInProgress.roll())
        } else {
          Phase.PickSlot(phase.game, phase.moveInProgress.roll())
        }
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

  fun handle(command: Command): StateMachine = StateMachine(reduce(phase, command), die)

  fun <P : Phase> phase(): P = phase as P

  private fun reduce(phase: Phase, command: Command): Phase {
    reducers.get(phase.javaClass to command.javaClass)?.let {
      return it.reduce(phase, command)
    }

    throw IllegalStateException("${phase} + ${command} can't be handled")
  }
}
