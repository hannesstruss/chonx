package chonx.cli2

import chonx.cli2.Command.AddPlayer
import chonx.core.PreGame
import kotlin.reflect.KClass

data class StateMachine(val phase: Phase) {
  companion object {
    val LegalTransitions: Map<KClass<out Phase>, Set<KClass<out Command>>> = mapOf(
        Phase.CollectPlayers::class to setOf(
            Command.AddPlayer::class,
            Command.BeginGame::class
        ),
        Phase.InGame::class to setOf(
            Command.RollDice::class
        ),
        Phase.InMove::class to setOf(
            Command.RollDice::class,
            Command.LockDice::class,
            Command.UnlockDice::class,
            Command.AcceptDice::class
        ),
        Phase.PickSlot::class to setOf(
            Command.PickSlot::class
        )
    )

    fun new() = StateMachine(Phase.CollectPlayers(PreGame.new()))
  }

  fun handle(command: Command): StateMachine {
    if (command.javaClass.kotlin !in LegalTransitions.get(phase.javaClass.kotlin)!!) {
      throw IllegalStateException("Can't handle ${command} in phase ${phase}")
    }

    val nextPhase: Phase = if (phase is Phase.CollectPlayers) {
      when (command) {
        is AddPlayer -> Phase.CollectPlayers(phase.preGame.addPlayer(command.name))
        else -> wrongPhase()
      }
    } else {
      wrongPhase()
    }

    return StateMachine(nextPhase)
  }

  fun <T> wrongPhase(): T {
    throw IllegalStateException("Wrong phase")
  }
}
