package chonx

import chonx.Slot.*

class Game private constructor(val players: List<Player>,
                               val currentPlayer: Player,
                               private val moves: List<Move>) {

  companion object {
    fun new(players: List<Player>) = Game(players, players[0], listOf())

    private val TOP_HALF_SLOTS = setOf(ACES, TWOS, THREES, FOURS, FIVES, SIXES)
  }

  fun move(roll: DiceRoll, slot: Slot): Game {
    val move = Move(currentPlayer, slot, roll)

    val nextPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size)
    return Game(players, nextPlayer, moves + move)
  }

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
}
