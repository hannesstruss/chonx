package chonx.core

data class MoveInProgress private constructor(
    private val die: Die,
    val player: Player,
    val rollsLeft: Int,
    private val dice: List<Int>,
    private val locks: Set<Int>
) {
  companion object {
    val InitialRolls = 3

    fun start(player: Player) = start(player, RandomDie())

    fun start(player: Player, die: Die): MoveInProgress {
      return MoveInProgress(die, player, InitialRolls, newRoll(die), setOf())
    }

    fun newRoll(die: Die): List<Int> =
        listOf(die.roll(), die.roll(), die.roll(), die.roll(), die.roll())
  }

  fun roll(): MoveInProgress {
    if (rollsLeft <= 0) {
      throw IllegalStateException("Exhausted all rolls.")
    }
    return copy(rollsLeft = rollsLeft - 1, dice = lockedRoll())
  }

  fun lock(indexOfDieToLock: Int) = copy(locks = locks + indexOfDieToLock)

  fun unlock(indexOfDieToUnlock: Int) = copy(locks = locks - indexOfDieToUnlock)

  fun isLocked(indexOfDie: Int) = indexOfDie in locks

  private fun lockedRoll(): List<Int> {
    return dice.mapIndexed { index, dieValue -> if (index in locks) dieValue else die.roll() }
  }

  fun dice() = dice
}
