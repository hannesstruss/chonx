package chonx.core

enum class Slot {
  ACES {
    override fun points(roll: DiceRoll) = numbers(1, roll)
  },

  TWOS {
    override fun points(roll: DiceRoll) = numbers(2, roll)
  },

  THREES {
    override fun points(roll: DiceRoll) = numbers(3, roll)
  },

  FOURS {
    override fun points(roll: DiceRoll) = numbers(4, roll)
  },

  FIVES {
    override fun points(roll: DiceRoll) = numbers(5, roll)
  },

  SIXES {
    override fun points(roll: DiceRoll) = numbers(6, roll)
  },

  THREE_OF_A_KIND {
    override fun points(roll: DiceRoll) =
        roll.dice()
            .groupBy { it }
            .values
            .any { it.size >= 3 }
            .let { if (it) roll.dice().sum() else 0 }
  },

  FOUR_OF_A_KIND {
    override fun points(roll: DiceRoll) =
        roll.dice()
            .groupBy { it }
            .values
            .any { it.size >= 4 }
            .let { if (it) roll.dice().sum() else 0 }
  },

  FULL_HOUSE {
    override fun points(roll: DiceRoll): Int {
      val isFullHouse = roll.dice().histogram().values.sorted() == listOf(2, 3)
      return if (isFullHouse) 25 else 0
    }
  },

  SMALL_STRAIGHT {
    override fun points(roll: DiceRoll) =
        if (roll.dice().isStraight(4)) 30 else 0

  },

  LARGE_STRAIGHT {
    override fun points(roll: DiceRoll) =
        if (roll.dice().isStraight(5)) 40 else 0
  },

  CHANCE {
    override fun points(roll: DiceRoll) = roll.dice().sum()
  },

  /** a.k.a. brand name */
  FIVE_OF_A_KIND {
    override fun points(roll: DiceRoll): Int {
      val distinct = roll.dice().distinct()
      return if (distinct.size == 1) 50 else 0
    }
  };

  companion object {
    fun byId(id: String): Slot? =
        values().find { it.name == id.toUpperCase() }
  }

  abstract fun points(roll: DiceRoll): Int

  fun List<Int>.isStraight(size: Int) =
      this
          .sorted()
          .pairs()
          .map { it.second - it.first }
          .dropWhile { it != 1 }
          .takeWhile { it == 1 }
          .count()
          .let { it >= size - 1 }

  fun numbers(number: Int, roll: DiceRoll) =
      roll.dice().filter { it == number }.sum()

  fun List<Int>.histogram(): Map<Int, Int> =
      this.groupBy { it }.mapValues { it.value.size }
}
