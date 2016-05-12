package chonx

class Game private constructor(val players: List<Player>,
                               val currentPlayer: Player,
                               private val moves: List<Move>) {

  companion object {
    fun new(players: List<Player>) = Game(players, players[0], listOf())
  }

  fun move(roll: DiceRoll, slot: Slot): Game {
    val move = Move(currentPlayer, slot, roll)

    val nextPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size)
    return Game(players, nextPlayer, moves + move)
  }

  fun points(player: Player): Int = moves
      .filter { it.player == player }
      .map { it.slot.points(it.diceRoll) }
      .sum()
}
