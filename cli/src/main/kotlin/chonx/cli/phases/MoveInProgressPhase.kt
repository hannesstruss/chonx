package chonx.cli.phases

import chonx.core.Game
import chonx.core.MoveInProgress

class MoveInProgressPhase(
    private val game: Game,
    private var move: MoveInProgress) : Phase {

  companion object {
    val LockRegex = """^lock (\d+)""".toRegex()
    val UnlockRegex = """^unlock (\d+)""".toRegex()
    val RollRegex = """roll""".toRegex()
    val DoneRegex = """done""".toRegex()
    val InfoRegex = """score""".toRegex()
  }

  override fun preCmd(): PhaseResult {
    println("${move.player.name}, your dice: ${fmtDice(move)}")
    return PhaseResult.Ready
  }

  override fun handleCmd(cmd: String): PhaseResult {
    LockRegex.find(cmd)?.let {
      val index = it.groupValues[1].toInt()
      move = move.lock(index - 1)
      println("Locked ${index}.")
      return PhaseResult.Ready
    }

    UnlockRegex.find(cmd)?.let {
      val index = it.groupValues[1].toInt()
      move = move.unlock(index - 1)
      println("Unlocked ${index}.")
      return PhaseResult.Ready
    }

    RollRegex.find(cmd)?.let {
      if (move.rollsLeft > 0) {
        move = move.roll()
        println("Rolled. Rolls left: ${move.rollsLeft}.")
      } else {
        println("No rolls left!")
      }
      return PhaseResult.Ready
    }

    InfoRegex.find(cmd)?.let {
      println("Your score: ${game.score(game.currentPlayer)}")
      return PhaseResult.Ready
    }

    DoneRegex.find(cmd)?.let {
      return PhaseResult.NextPhase(PickSlotPhase(game, move))
    }

    return PhaseResult.Ready
  }

  private fun fmtDice(move: MoveInProgress): String {
    return move.dice()
        .mapIndexed { index, die ->
          if (move.isLocked(index)) {
            "(${die})"
          } else {
            die.toString()
          }
        }
        .joinToString(", ")
  }
}
