package chonx.cli2

import chonx.cli2.Command.AddPlayer
import chonx.cli2.Command.BeginGame
import chonx.cli2.Phase.CollectPlayers
import chonx.core.Game
import chonx.core.PreGame

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

private val reducers = listOf(

    reducer<CollectPlayers, AddPlayer> { phase, command ->
      CollectPlayers(phase.preGame.addPlayer(command.name))
    },

    reducer<CollectPlayers, BeginGame> { phase, command ->
      Phase.InGame(Game.new(phase.preGame.players()))
    },

    reducer<Phase.InGame, Command.RollDice> { phase, command ->
      Phase.InMove(phase.game, phase.game.roll())
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
      Phase.InMove(phase.game, phase.moveInProgress.roll())
    }

).map { reducer ->
  Pair(reducer.phaseClass, reducer.commandClass) to reducer
}.toMap()

@Suppress("UNCHECKED_CAST")
fun <P : Phase> reduce(phase: Phase, command: Command): P {
  reducers.get(phase.javaClass to command.javaClass)?.let {
    return it.reduce(phase, command) as P
  }

  throw IllegalStateException("${phase} + ${command} can't be handled")
}

@Suppress("UNCHECKED_CAST")
data class StateMachine(val phase: Phase) {
  companion object {
    fun new() = StateMachine(CollectPlayers(PreGame.new()))
  }

  fun handle(command: Command): StateMachine = StateMachine(reduce(phase, command))

  fun <P : Phase> phase(): P = phase as P
}
