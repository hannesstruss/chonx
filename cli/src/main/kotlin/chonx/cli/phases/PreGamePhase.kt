package chonx.cli.phases

import chonx.core.PreGame

class PreGamePhase : Phase {
  private var preGame = PreGame.new()

  override fun preCmd(): PhaseResult {
    println("Enter name of ${nth(preGame.players.size + 1)} player (or 'start' to begin):")
    return PhaseResult.Ready
  }

  override fun handleCmd(cmd: String): PhaseResult = when(cmd) {
    "start" -> PhaseResult.NextPhase(GamePhase.new(preGame.players))
    else -> {
      preGame = preGame.addPlayer(cmd)
      PhaseResult.Ready
    }
  }

  private fun nth(n: Int): String = when {
    n <= 0 -> throw IllegalArgumentException("Can't handle ${n}")
    n % 100 in 11..13 -> "${n}th"
    n % 10 == 1 -> "${n}st"
    n % 10 == 3 -> "${n}rd"
    n % 10 == 2 -> "${n}nd"
    else -> "${n}th"
  }
}
