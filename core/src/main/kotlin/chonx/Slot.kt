package chonx

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
    override fun points(roll: DiceRoll): Int {
      throw UnsupportedOperationException()
    }
  },

  FULL_HOUSE {
    override fun points(roll: DiceRoll): Int {
      val isFullHouse = roll.dice().histogram().values.sorted() == listOf(2, 3)
      return if (isFullHouse) 25 else 0
    }
  },

  SMALL_STRAIGHT {
    override fun points(roll: DiceRoll) =
        roll.dice().sorted().sum()
  },

  LARGE_STRAIGHT {
    override fun points(roll: DiceRoll): Int {
      throw UnsupportedOperationException()
    }
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

  abstract fun points(roll: DiceRoll): Int

  fun numbers(number: Int, roll: DiceRoll) =
      roll.dice().filter { it == number }.sum()

  fun List<Int>.histogram(): Map<Int, Int> =
      this.groupBy { it }.mapValues { it.value.size }
}
