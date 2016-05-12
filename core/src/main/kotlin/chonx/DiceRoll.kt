package chonx

data class DiceRoll(val die1: Int, val die2: Int, val die3: Int, val die4: Int, val die5: Int) {
  init {
    check(die1)
    check(die2)
    check(die3)
    check(die4)
    check(die5)
  }

  fun dice() = listOf(die1, die2, die3, die4, die5)

  private fun check(die: Int) {
    if (die < 1 || die > 6) {
      throw IllegalArgumentException("Illegal die value: $die")
    }
  }
}
