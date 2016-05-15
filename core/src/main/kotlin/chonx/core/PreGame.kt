package chonx.core

data class PreGame private constructor(val players: Set<Player>) {
  companion object {
    fun new() = PreGame(setOf())
  }

  fun addPlayer(name: String) = copy(players = players + Player(name))

  fun players() = players

  val numPlayers: Int
    get() = players.size
}
