package chonx

fun main(args: Array<String>) {
  val hannes = Player("Hannes")
  val game = Game.new(listOf(hannes))

  val roll = DiceRoll(1, 2, 5, 4, 6)
  val newGame = game.move(roll, Slot.CHANCE)
}
