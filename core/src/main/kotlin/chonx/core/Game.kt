package chonx.core

import java.util.Collections

data class Game constructor(val players: List<Player>,
                            val currentPlayer: Player,
                            private val moves: List<Move> = listOf(),
                            private val die: Die = RandomDie()) {

  companion object {
    fun new(players: Set<Player>, die: Die = RandomDie()): Game {
      val playerList = players.toMutableList()
      Collections.shuffle(playerList)
      return Game(playerList, playerList[0], listOf(), die)
    }
  }

  init {
    if (currentPlayer !in players) {
      throw IllegalArgumentException("Player ${currentPlayer.name} not in players")
    }
  }

  fun move(moveInProgress: MoveInProgress, slot: Slot): Game {
    if (!isLegalMove(moveInProgress.player, slot)) {
      throw IllegalArgumentException("Illegal move: ${moveInProgress.player}/${slot}")
    }

    val move = Move(currentPlayer, slot, DiceRoll.fromList(moveInProgress.dice()))

    val nextPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size)
    return copy(currentPlayer = nextPlayer, moves = moves + move)
  }

  fun roll() = MoveInProgress.start(currentPlayer, die)

  fun getSlotOptions(player: Player): Set<Slot> {
    return Slot.values().toSet().minus(
        moves.filter { it.player == player }.map { it.slot }.toSet())
  }

  fun score(player: Player) = calculateScore(moves, player)

  fun isLegalMove(player: Player, slot: Slot) =
      player == currentPlayer &&
          moves.filter { it.player == currentPlayer && it.slot == slot }.isEmpty()

  fun hasEnded() =
      moves.isNotEmpty() && moves
          .groupBy { it.player }
          .values
          .all { it.size == Slot.values().size }
}
