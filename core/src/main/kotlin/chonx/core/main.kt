package chonx.core

fun main(args: Array<String>) {
  val preGame = PreGame.new()
      .addPlayer("hannesstruss")
      .addPlayer("felixgoldstein")

  var game = Game.new(preGame.players())

  var move = game.roll(RandomDie())
  move = move.lock(0)
  move = move.lock(1)
  move = move.roll()

  game = game.move(move, Slot.CHANCE)
}
