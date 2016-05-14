package chonx.core

import chonx.core.Slot.ACES
import chonx.core.Slot.FIVES
import chonx.core.Slot.FOURS
import chonx.core.Slot.SIXES
import chonx.core.Slot.THREES
import chonx.core.Slot.TWOS
import java.util.Collections

class Game private constructor(val players: List<Player>,
                               val currentPlayer: Player,
                               private val moves: List<Move>) {

  companion object {
    fun new(players: Set<Player>): Game {
      val playerList = players.toMutableList()
      Collections.shuffle(playerList)
      return Game(playerList, playerList[0], listOf())
    }

    private val TOP_HALF_SLOTS = setOf(ACES, TWOS, THREES, FOURS, FIVES, SIXES)
  }

  fun move(roll: DiceRoll, slot: Slot): Game {
    val move = Move(currentPlayer, slot, roll)

    val nextPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size)
    return Game(players, nextPlayer, moves + move)
  }

  fun roll() = MoveInProgress.start(currentPlayer)

  fun score(player: Player): Int {
    val playerMoves = moves.filter { it.player == player }
        .partition { it.slot in TOP_HALF_SLOTS }

    val topHalfScore = playerMoves.first
        .map { it.slot.points(it.diceRoll) }
        .sum()
        .let { if (it > 63) it + 35 else it }

    val bottomHalfScore = playerMoves.second.map { it.slot.points(it.diceRoll) }.sum()

    return topHalfScore + bottomHalfScore
  }

  fun isLegalMove(slot: Slot) =
      moves.filter { it.player == currentPlayer && it.slot == slot }.isEmpty()

  fun hasEnded() =
      moves.isNotEmpty() && moves
          .groupBy { it.player }
          .values
          .all { it.size == Slot.values().size }
}
