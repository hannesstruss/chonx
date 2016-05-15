package chonx.cli.phases

import chonx.core.Game
import chonx.core.MoveInProgress
import chonx.core.Slot

class PickSlotPhase(
    val game: Game,
    val move: MoveInProgress) : Phase {

  companion object {
    private val SlotIDs = Slot.values().map { it.name.toLowerCase() }.toSet()
  }

  override fun preCmd(): PhaseResult {
    println("${game.currentPlayer.name}, those are your dice: ${move.dice().joinToString(", ")}")
    println("Which slot do you pick? (type 'help' for help)")
    return PhaseResult.Ready
  }

  override fun handleCmd(cmd: String): PhaseResult {
    when {
      cmd == "help" -> printHelp()
      cmd in SlotIDs -> {
        val slot = Slot.byId(cmd)!!
        val phase = GamePhase.move(game, move, slot)
        return PhaseResult.NextPhase(phase)
      }
    }
    return PhaseResult.Ready
  }

  private fun printHelp() {
    val opts = game.getSlotOptions(game.currentPlayer).map { it.name.toLowerCase() }.joinToString(", ")
    println("Your options: $opts")
  }
}
