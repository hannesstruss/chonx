package chonx.core

private val TOP_HALF_SLOTS = setOf(Slot.ACES, Slot.TWOS, Slot.THREES, Slot.FOURS, Slot.FIVES, Slot.SIXES)

fun calculateScore(moves: List<Move>, player: Player): Int {
  val playerMoves = moves.filter { it.player == player }
      .partition { it.slot in TOP_HALF_SLOTS }

  val topHalfScore = playerMoves.first
      .map { it.slot.points(it.diceRoll) }
      .sum()
      .let { if (it > 63) it + 35 else it }

  val bottomHalfScore = playerMoves.second.map { it.slot.points(it.diceRoll) }.sum()

  return topHalfScore + bottomHalfScore
}
