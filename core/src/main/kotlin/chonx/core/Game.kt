package chonx.core

import java.util.Collections

data class Game constructor(val players: List<Player>,
                            val currentPlayer: Player,
                            private val moves: List<Move> = listOf()) {

  companion object {
    fun new(players: Set<Player>): Game {
      if (players.size == 0) {
        throw NotEnoughPlayersException()
      }

      val playerList = players.toMutableList()
      Collections.shuffle(playerList)
      return Game(playerList, playerList[0], listOf())
    }
  }

  init {
    if (currentPlayer !in players) {
      throw IllegalArgumentException("Player ${currentPlayer.name} not in players")
    }
  }

  fun move(moveInProgress: MoveInProgress, slot: Slot): Game {
    if (!canMove(moveInProgress.player)) {
      throw IllegalMoveException("It's not ${moveInProgress.player}'s turn")
    }

    if (!isSlotFree(moveInProgress.player, slot)) {
      throw SlotAlreadyFilledException("Slot already filled for ${moveInProgress.player.name}: ${slot.name}")
    }

    val move = Move(currentPlayer, slot, DiceRoll.fromList(moveInProgress.dice()))

    val nextPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size)
    return copy(currentPlayer = nextPlayer, moves = moves + move)
  }

  fun roll(die: Die) = MoveInProgress.start(currentPlayer, die)

  fun getSlotOptions(player: Player): Set<Slot> {
    return Slot.values().toSet().minus(
        moves.filter { it.player == player }.map { it.slot }.toSet())
  }

  fun score(player: Player) = calculateScore(moves, player)

  fun canMove(player: Player) = player == currentPlayer

  fun isSlotFree(player: Player, slot: Slot) =
          moves.filter { it.player == player && it.slot == slot }.isEmpty()

  fun hasEnded() =
      moves.isNotEmpty() && moves
          .groupBy { it.player }
          .values
          .all { it.size == Slot.values().size }
}
