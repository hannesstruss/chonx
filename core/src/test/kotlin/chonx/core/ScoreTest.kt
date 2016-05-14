package chonx.core

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ScoreTest {
  val hannes = Player("hannes")
  val felix = Player("felix")

  @Test fun `should calculate score`() {
    val moves = listOf(
        Move(hannes, Slot.CHANCE, DiceRoll(1, 1, 1, 1, 1))
    )

    assertThat(calculateScore(moves, hannes)).isEqualTo(5)
    assertThat(calculateScore(moves, felix)).isEqualTo(0)
  }

  @Test fun shouldCalculateUpperHalfBonus() {
    val moves = listOf(
        Move(hannes, Slot.ACES, DiceRoll(1, 1, 1, 1, 1)),
        Move(hannes, Slot.TWOS, DiceRoll(2, 2, 2, 2, 2)),
        Move(hannes, Slot.THREES, DiceRoll(3, 3, 3, 3, 3)),
        Move(hannes, Slot.FOURS, DiceRoll(4, 4, 4, 4, 4)),
        Move(hannes, Slot.FIVES, DiceRoll(5, 5, 5, 5, 5)),
        Move(hannes, Slot.SIXES, DiceRoll(6, 6, 6, 6, 6))
    )

    assertThat(calculateScore(moves, hannes)).isEqualTo(140)
  }
}
