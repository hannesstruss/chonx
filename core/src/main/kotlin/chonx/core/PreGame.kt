package chonx.core

data class PreGame private constructor(val players: Set<Player>) {
  companion object {
    fun new() = PreGame(setOf())
  }

  fun addPlayer(player: Player) = copy(players = players + player)

  fun players() = players

  val numPlayers: Int
    get() = players.size
}
