package chonx.core

class PreGame private constructor(val players: Set<Player>) {
  companion object {
    fun new() = PreGame(setOf())
  }

  fun addPlayer(name: String): PreGame = PreGame(players + Player(name))

  fun players() = players

  val numPlayers: Int
    get() = players.size
}
